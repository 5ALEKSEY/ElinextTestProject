package com.ak.elinexttestproject.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.elinexttestproject.R
import com.ak.elinexttestproject.di.AppComponent
import com.ak.elinexttestproject.until.injectViewModel
import com.ak.elinexttestproject.view.list.ImagesListAdapter
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        private const val ROWS_COUNT = 10
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainActivityViewModel

    private lateinit var imagesAdapter: ImagesListAdapter
    private lateinit var imagesRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponent.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = injectViewModel(viewModelFactory)

        with(viewModel) {
            subscribeToImagesListData().observe(this@MainActivity) {
                imagesAdapter.insertData(it)
            }
            subscribeToErrorMessage().observe(this@MainActivity) {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
            onInitialImagesLoadNeeds()
        }
    }

    private fun initViews() {
        imagesAdapter = ImagesListAdapter()
        imagesRecycler = findViewById<RecyclerView>(R.id.rv_images_list).apply {
            val gridLayoutManager = GridLayoutManager(
                this@MainActivity,
                ROWS_COUNT,
                RecyclerView.HORIZONTAL,
                false
            )
            layoutManager = gridLayoutManager
            adapter = imagesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = gridLayoutManager.childCount
                    val totalItemCount = gridLayoutManager.itemCount
                    val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 20
                        && dx > 0
                    ) {
                        viewModel.onLoadNewImagesNeeds()
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.images_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item == null) {
            super.onOptionsItemSelected(item)
        } else {
            when(item.itemId) {
                R.id.im_add_new -> {
                    viewModel.onAddNewImage()
                    true
                }
                R.id.im_reload_all -> {
                    imagesRecycler.scrollToPosition(0)
                    viewModel.onReloadAllImages()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }
}
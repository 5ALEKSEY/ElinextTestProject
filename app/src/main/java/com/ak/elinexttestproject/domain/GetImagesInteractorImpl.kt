package com.ak.elinexttestproject.domain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ak.elinexttestproject.data.IImagesRepo
import com.ak.elinexttestproject.until.addToComposite
import com.ak.elinexttestproject.view.list.ImageListItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetImagesInteractorImpl @Inject constructor(
    private val imagesRepo: IImagesRepo
) : IGetImagesInteractor {

    companion object {
        private const val TAG = "GetImagesInterImpl"
        private const val RELOAD_ALL_IMAGES_COUNT = 140
        private const val MAX_IMAGES_COUNT = RELOAD_ALL_IMAGES_COUNT
        private const val INITIAL_LOAD_COUNT = 100
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_POSITION = 0
    }

    private lateinit var compositeDisposable: CompositeDisposable
    private val imagesItems = ArrayList<ImageListItem>(INITIAL_LOAD_COUNT)
    private val imagesListLD = MutableLiveData<List<ImageListItem>>()
    private val errorMessageLD = MutableLiveData<String>()

    override fun loadInitialData() {
        loadImages(INITIAL_LOAD_COUNT, INITIAL_LOAD_POSITION)
    }

    @Synchronized
    override fun reloadAll() {
        compositeDisposable.clear()
        imagesRepo.destroy()
        imagesItems.clear()

        loadImages(RELOAD_ALL_IMAGES_COUNT, INITIAL_LOAD_POSITION)
    }

    override fun loadNext() {
        val currentListSize = imagesItems.size
        if (currentListSize + PAGE_SIZE >= MAX_IMAGES_COUNT) {
            val difference = MAX_IMAGES_COUNT - currentListSize
            if (difference > 0) {
                loadImages(difference, currentListSize)
            }
        } else {
            loadImages(PAGE_SIZE, currentListSize)
        }
    }

    override fun forceLoad(count: Int) {
        loadImages(count, imagesItems.size)
    }

    override fun subscribeToDataList() = imagesListLD

    override fun subscribeToError() = errorMessageLD

    override fun applyComposite(compositeDisposable: CompositeDisposable) {
        this.compositeDisposable = compositeDisposable
    }

    private fun loadImages(loadSize: Int, fromPosition: Int) {
        Log.i(TAG, "loadImages=>loadSize=$loadSize, fromPosition=$fromPosition")

        val listOfRequests = arrayListOf<Single<String>>()
        repeat(loadSize) { loadPosition ->
            val newPosition = fromPosition + loadPosition
            imagesItems.add(newPosition, ImageListItem(newPosition))

            val request = imagesRepo.getRandomImageUrl()
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    val newItem = imagesItems[newPosition].copy(imageUrl = it, isLoading = false)
                    imagesItems[newPosition] = newItem
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    imagesListLD.value = imagesItems
                }
            listOfRequests.add(request)
        }
        imagesListLD.value = imagesItems


        Single.merge(listOfRequests)
            .subscribeOn(Schedulers.io())
            .subscribe({}, {
                errorMessageLD.value = "Error during loading. Try again later"
                imagesListLD.value = emptyList()
            })
            .addToComposite(compositeDisposable)
    }
}
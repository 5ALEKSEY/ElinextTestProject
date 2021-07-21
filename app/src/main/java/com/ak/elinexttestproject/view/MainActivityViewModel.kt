package com.ak.elinexttestproject.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ak.elinexttestproject.di.AppComponent
import com.ak.elinexttestproject.domain.IGetImagesInteractor
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val getImagesInteractor: IGetImagesInteractor
) : ViewModel() {

    companion object {
        private const val TAG = "MainActivityVM"
        private const val SINGLE_LOAD_COUNT = 1
    }

    private val disposables by lazy(::CompositeDisposable)

    init {
        AppComponent.get().inject(this)
        getImagesInteractor.applyComposite(disposables)
    }

    fun subscribeToImagesListData() = getImagesInteractor.subscribeToDataList()
    fun subscribeToErrorMessage() = getImagesInteractor.subscribeToError()

    fun onInitialImagesLoadNeeds() {
        Log.i(TAG, "onInitialImagesLoadNeeds")
        getImagesInteractor.loadInitialData()
    }

    fun onReloadAllImages() {
        Log.i(TAG, "onReloadAllImages")
        getImagesInteractor.reloadAll()
    }

    fun onAddNewImage() {
        Log.i(TAG, "onAddNewImage")
        getImagesInteractor.forceLoad(SINGLE_LOAD_COUNT)
    }

    fun onLoadNewImagesNeeds() {
        Log.i(TAG, "onLoadNewImagesNeeds")
        getImagesInteractor.loadNext()
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared")
        super.onCleared()
        disposables.clear()
    }
}
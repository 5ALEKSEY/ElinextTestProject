package com.ak.elinexttestproject.domain

import androidx.lifecycle.LiveData
import com.ak.elinexttestproject.view.list.ImageListItem
import io.reactivex.disposables.CompositeDisposable

interface IGetImagesInteractor {
    fun loadInitialData()
    fun reloadAll()
    fun loadNext()
    fun forceLoad(count: Int)
    fun subscribeToDataList(): LiveData<List<ImageListItem>>
    fun subscribeToError(): LiveData<String>
    fun applyComposite(compositeDisposable: CompositeDisposable)
}
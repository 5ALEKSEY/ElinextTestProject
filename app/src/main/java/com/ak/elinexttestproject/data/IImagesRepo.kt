package com.ak.elinexttestproject.data

import io.reactivex.Single

interface IImagesRepo {
    fun getRandomImageUrl(): Single<String>
    fun destroy()
}
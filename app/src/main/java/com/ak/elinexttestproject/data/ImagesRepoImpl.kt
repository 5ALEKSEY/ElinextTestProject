package com.ak.elinexttestproject.data

import android.os.Looper
import android.util.Log
import io.reactivex.Single
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

class ImagesRepoImpl @Inject constructor(
    private val httpClient: OkHttpClient,
    private val request: Request
) : IImagesRepo {

    companion object {
        private const val TAG = "ImagesRepoImpl"
    }

    override fun getRandomImageUrl(): Single<String> = Single.create {
        Log.i(TAG, "isMainThread: ${Looper.getMainLooper().thread === Thread.currentThread()}")
        if (it.isDisposed) {
            return@create
        }
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!it.isDisposed) it.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!it.isDisposed) it.onSuccess(response.request.url.toString())
            }
        })
    }

    override fun destroy() {
        httpClient.dispatcher.cancelAll()
    }
}
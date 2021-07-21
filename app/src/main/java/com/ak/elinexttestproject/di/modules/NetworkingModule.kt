package com.ak.elinexttestproject.di.modules

import android.content.Context
import android.util.Log
import com.ak.elinexttestproject.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkingModule {

    companion object {
        private const val TAG = "NetworkingModule"
        private const val CACHE_FILE_NAME = "RESPONSE"
        private const val CACHE_SIZE = (1024 * 1024 * 20).toLong()
        private const val CONNECT_TIMEOUT_SECONDS = 15L
        private const val READ_CONNECT_TIMEOUT_SECONDS = 20L
        private const val WRITE_CONNECT_TIMEOUT_SECONDS = 20L
    }

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        return File(context.cacheDir, CACHE_FILE_NAME).run {
            Cache(this, CACHE_SIZE)
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String? ->
            Log.i(TAG, "message: $message")
        }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    @Singleton
    fun okHttpClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().run {
            connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(WRITE_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(READ_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }
            retryOnConnectionFailure(true)
            cache(cache)

            build()
        }
    }

    @Provides
    @Singleton
    fun provideRequest(): Request {
        return Request.Builder()
            .url(BuildConfig.API_URL)
            .build()
    }
}
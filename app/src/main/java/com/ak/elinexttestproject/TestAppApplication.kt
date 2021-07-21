package com.ak.elinexttestproject

import android.app.Application
import android.content.Context
import com.ak.elinexttestproject.di.AppComponent

class TestAppApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        AppComponent.initialize().apply {
            inject(this@TestAppApplication)
        }
    }
}
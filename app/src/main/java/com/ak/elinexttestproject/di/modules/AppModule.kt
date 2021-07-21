package com.ak.elinexttestproject.di.modules

import android.content.Context
import com.ak.elinexttestproject.TestAppApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = TestAppApplication.appContext.applicationContext
}
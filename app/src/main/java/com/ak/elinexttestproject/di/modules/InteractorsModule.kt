package com.ak.elinexttestproject.di.modules

import com.ak.elinexttestproject.domain.GetImagesInteractorImpl
import com.ak.elinexttestproject.domain.IGetImagesInteractor
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface InteractorsModule {
    @Binds
    @Singleton
    fun bindGetImagesInteractor(getImagesInteractorImpl: GetImagesInteractorImpl): IGetImagesInteractor
}
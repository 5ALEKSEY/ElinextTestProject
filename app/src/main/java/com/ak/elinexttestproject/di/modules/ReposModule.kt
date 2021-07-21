package com.ak.elinexttestproject.di.modules

import com.ak.elinexttestproject.data.IImagesRepo
import com.ak.elinexttestproject.data.ImagesRepoImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ReposModule {
    @Binds
    @Singleton
    fun bindImagesRepo(imagesRepoImpl: ImagesRepoImpl): IImagesRepo
}
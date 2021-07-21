package com.ak.elinexttestproject.di.modules.viewmodule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ak.elinexttestproject.view.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelInjectKey(MainActivityViewModel::class)
    abstract fun injectMainVM(viewModel: MainActivityViewModel): ViewModel
}
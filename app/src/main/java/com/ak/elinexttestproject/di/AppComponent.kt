package com.ak.elinexttestproject.di

import com.ak.elinexttestproject.TestAppApplication
import com.ak.elinexttestproject.di.modules.AppModule
import com.ak.elinexttestproject.di.modules.InteractorsModule
import com.ak.elinexttestproject.di.modules.NetworkingModule
import com.ak.elinexttestproject.di.modules.ReposModule
import com.ak.elinexttestproject.di.modules.viewmodule.ViewModelsModule
import com.ak.elinexttestproject.view.MainActivity
import com.ak.elinexttestproject.view.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NetworkingModule::class,
        ViewModelsModule::class,
        InteractorsModule::class,
        ReposModule::class
    ]
)
@Singleton
abstract class AppComponent {

    companion object {
        @Volatile
        private var appComponent: AppComponent? = null

        fun get() = requireNotNull(appComponent) {
            "AppComponent is null. initialize() should be called before"
        }

        fun initialize(): AppComponent {
            if (appComponent == null) {
                appComponent = DaggerAppComponent.builder()
                    .appModule(AppModule())
                    .build()
            }

            return requireNotNull(appComponent) {
                "something was wrong with app component realization"
            }
        }
    }

    abstract fun inject(app: TestAppApplication)
    abstract fun inject(activity: MainActivity)
    abstract fun inject(viewModel: MainActivityViewModel)
}
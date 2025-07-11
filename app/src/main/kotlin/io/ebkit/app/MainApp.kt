package io.ebkit.app

import com.google.android.material.color.DynamicColors
import com.kongzue.baseframework.BaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApp : BaseApp<MainApp>(), KoinComponent {

    override fun init() {
        DynamicColors.applyToActivitiesIfAvailable(this@MainApp)
        startKoin {
            androidLogger()
            androidContext(androidContext = this@MainApp)
            modules(modules = coreModule)
            modules(modules = module { single { this@MainApp } })
        }
    }
}
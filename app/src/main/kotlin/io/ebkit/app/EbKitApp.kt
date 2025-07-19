package io.ebkit.app

import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.android.material.color.DynamicColors
import com.kongzue.baseframework.BaseApp
import io.ebkit.app.di.appModules
import io.ebkit.app.hybrid.FlutterLoader
import io.ebkit.app.hybrid.loadFlutter
import io.ebkit.app.server.RunningService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.lsposed.hiddenapibypass.HiddenApiBypass

class EbKitApp : BaseApp<EbKitApp>() {

    override fun init() {
        DynamicColors.applyToActivitiesIfAvailable(this@EbKitApp)
        loadFlutter()
        startKoin {
            androidLogger()
            androidContext(androidContext = this@EbKitApp)
            modules(appModules)
            modules(
                modules = module {
                    single {
                        this@EbKitApp
                    }
                }
            )
        }
        startForeground()
    }

    override fun initSDKs() {
        super.initSDKs()
        // 初始化SDK
    }

    override fun initSDKInitialized() {
        super.initSDKInitialized()
        // 初始化完成

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    private fun startForeground() {
        val intent = Intent(
            this@EbKitApp,
            RunningService::class.java,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
package io.ebkit.app

import android.content.Context
import android.os.Build
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import com.kongzue.baseframework.BaseApp
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import org.lsposed.hiddenapibypass.HiddenApiBypass

class MainApplication: BaseApp<MainApplication>(), FlutterBoostDelegate {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    override fun init() {
        FlutterBoost.instance().setup(this@MainApplication, this@MainApplication) {}
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) {}

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions) {
        val intent =
            FlutterBoostActivity.CachedEngineIntentBuilder(FlutterBoostActivity::class.java)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .destroyEngineWithActivity(false)
                .uniqueId(options.uniqueId())
                .url(options.pageName())
                .urlParams(options.arguments())
                .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }
}
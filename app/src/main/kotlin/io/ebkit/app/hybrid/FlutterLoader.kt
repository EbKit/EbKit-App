package io.ebkit.app.hybrid

import android.app.Activity
import android.app.Application
import android.content.Context
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.plugins.FlutterPlugin

fun Application.loadFlutter() {
    FlutterLoader().load(context = this@loadFlutter)
}

fun Activity.loadFlutterFragment(): FlutterFragment {
    return FlutterFragment
        .withCachedEngine("ebkit_engine")
        .destroyEngineWithFragment(false)
        .renderMode(RenderMode.texture)
        .build()
}

class FlutterLoader {
    fun load(context: Context) {
        // 初始化Flutter引擎
        FlutterEngine(context).let { engine ->
            engine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
            engine.plugins.add(mPlugin)
            FlutterEngineCache.getInstance().put("ebkit_engine", engine)
        }
    }

    private val mPlugin: FlutterPlugin = object : FlutterPlugin {

        override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

        }

        override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

        }
    }
}
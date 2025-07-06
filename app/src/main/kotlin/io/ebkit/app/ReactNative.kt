package io.ebkit.app

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

class ReactNative : ReactActivity() {

    override fun getMainComponentName(): String {
        return "ebkit_app"
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return DefaultReactActivityDelegate(
            activity = this@ReactNative,
            mainComponentName = mainComponentName,
            fabricEnabled = fabricEnabled,
        )
    }
}
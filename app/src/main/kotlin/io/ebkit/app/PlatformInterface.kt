package io.ebkit.app

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class PlatformInterface : ContextWrapper, KoinComponent {

    private val context: Context by inject<Context>()

    constructor(token: String) : super(null) {
        attachBaseContext(context)
        instanceTokens[this@PlatformInterface] = token
    }

    interface ICompanion {
        fun verifyToken(instance: PlatformInterface, token: String)
    }

    companion object : ICompanion {
        private var instanceTokens: MutableMap<PlatformInterface, String> =
            mutableMapOf<PlatformInterface, String>()

        override fun verifyToken(instance: PlatformInterface, token: String) {
            if (instanceTokens[instance] != token) throwTokenError()
        }

        private fun throwTokenError(): Nothing {
            error(message = "Verify Token Error")
        }
    }
}


interface IImpl {

    fun testFun()
}

abstract class EbKitPlatform : PlatformInterface, IImpl {

    constructor() : super(token = TOKEN)

    companion object {
        private const val TOKEN: String = "EbKitPlatform"

        private var mInstance: EbKitPlatform = DefaultInstance()

        var instance: EbKitPlatform
            get() = mInstance
            set(newInstance) {
                verifyToken(instance = newInstance, token = TOKEN)
                mInstance = newInstance
            }
    }
}

class DefaultInstance : EbKitPlatform() {


    override fun testFun() {
        error("未实现 testFun 接口")
    }
}

class EbKitImpl : EbKitPlatform() {

    override fun testFun() {
        Toast.makeText(this@EbKitImpl, "testFun", Toast.LENGTH_SHORT).show()
    }

}

class TestCls {

    fun ok() {
        EbKitPlatform.instance = EbKitImpl()
        EbKitPlatform.instance.testFun()
    }
}
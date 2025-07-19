package io.ebkit.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.window.core.layout.WindowWidthSizeClass
import coil.annotation.ExperimentalCoilApi
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.PermissionUtils
import com.google.android.material.appbar.MaterialToolbar
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.JumpParameter
import io.ebkit.app.MainActivity.Companion.AUTO_HIDE
import io.ebkit.app.MainActivity.Companion.AUTO_HIDE_DELAY_MILLIS
import io.ebkit.app.hybrid.FlutterAdapter
import io.ebkit.app.hybrid.loadFlutterFragment
import io.ebkit.app.ui.components.StateCard
import io.ebkit.app.ui.components.StateCardStyle
import io.ebkit.app.ui.screen.MPScreen
import io.ebkit.app.ui.theme.EbKitTheme
import io.ebkit.app.ui.view.HybridComposeView
import io.ebkit.app.ui.view.OverlayView
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import kotlinx.serialization.Serializable
import java.security.MessageDigest

class MainActivity : BaseActivity() {










    /** 插件绑定器. */
    private var mBinding: PluginBinding? = null

    /** 插件列表. */
    private var mPluginList: ArrayList<EcosedPlugin>? = null


    /** Activity */
    private var mActivity: Activity? = null

    /** 生命周期 */
    private var mLifecycle: Lifecycle? = null

    /** 供引擎使用的基本调试布尔值 */
    private val mBaseDebug: Boolean by lazy {
        AppUtils.isAppDebug()
    }

    /** 全局调试布尔值 */
    private var mFullDebug: Boolean = false

    /** 此服务意图 */
    private lateinit var mEcosedServicesIntent: Intent

//    /** 服务AIDL接口 */
//    private var mAIDL: IFeOSdk? = null

    /** 服务绑定状态 */
    private var mIsBind: Boolean = false

    /** 调试对话框 */
    private lateinit var mDebugDialog: AlertDialog

    /** 工具栏 */
    private lateinit var mToolbar: Toolbar

    private val hideHandler = Handler(Looper.myLooper()!!)

    private val showPart2Runnable = Runnable {
        getActionBar()?.show()
    }

    /** 工具栏显示状态 */
    private var isVisible: Boolean = false

    private val hideRunnable = Runnable {
        hide()
    }

    private val delayHideTouchListener: View.OnTouchListener =
        View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) delayedHide()
                MotionEvent.ACTION_UP -> view.performClick()
                else -> {}
            }
            return@OnTouchListener false
        }

    private var mFlutterFragment: FlutterFragment? = null



    override fun resetContentView(): View = mViewFactory.getContentFrame

    override fun initViews() = setLifeCircleListener(mLifecycleDelegate)

    override fun initDatas(parameter: JumpParameter?) = Unit

    override fun setEvents() = Unit

    override fun onPostResume() {
        super.onPostResume()
        mFlutterFragment?.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mFlutterFragment?.onNewIntent(intent)
    }

//    override fun onBackPressed() {
//        mFlutterFragment.onBackPressed()
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mFlutterFragment?.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        mFlutterFragment?.onActivityResult(
            requestCode, resultCode, data
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        mFlutterFragment?.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        mFlutterFragment?.onTrimMemory(level)
    }


    private val mLifecycleDelegate: LifeCircleListener = object : LifeCircleListener() {

        /**
         * OnCreate
         */
        override fun onCreate() = onScope(
            receiver = { myselfScope { this } },
            scope = { bridgeScope { this } },
            throws = { error(it) },
        ) {
            super.onCreate()
            onCreate(activity = it)
        }

        /**
         * OnDestroy
         */
        override fun onDestroy() = onScope(
            receiver = { myselfScope { this } },
            scope = { bridgeScope { this } },
            throws = { error(it) },
        ) {
            super.onDestroy()
            onDestroy()
        }
    }

    /**
     * Flutter插件代理
     */
    private interface FlutterPluginProxy {

        /** 创建 */
        fun FlutterPluginProxy.onCreate(activity: Activity)

        /** 销毁 */
        fun FlutterPluginProxy.onDestroy()

        fun FlutterPluginProxy.onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
        ): Boolean

        /** 方法调用 */
        fun FlutterPluginProxy.onMethodCall(
            call: MethodCallProxy,
            result: ResultProxy,
        )
    }

    /**
     * 方法调用代理
     */
    private interface MethodCallProxy {

        /** 方法名代理 */
        val methodProxy: String

        /** 传入参数代理 */
        val bundleProxy: Bundle
    }

    /**
     * 返回内容代理
     */
    private interface ResultProxy {

        /**
         * 处理成功结果.
         * @param resultProxy 处理成功结果,注意可能为空.
         */
        fun success(resultProxy: Any?)

        /**
         * 处理错误结果.
         * @param errorCodeProxy 错误代码.
         * @param errorMessageProxy 错误消息,注意可能为空.
         * @param errorDetailsProxy 详细信息,注意可能为空.
         */
        fun error(
            errorCodeProxy: String,
            errorMessageProxy: String?,
            errorDetailsProxy: Any?,
        )

        /**
         * 处理对未实现方法的调用.
         */
        fun notImplemented()
    }

    /**
     * 用于调用方法的接口.
     */
    private interface EcosedMethodCall {

        /**
         * 要调用的方法名.
         */
        val method: String?

        /**
         * 要传入的参数.
         */
        val bundle: Bundle?
    }

    /**
     * 方法调用结果回调.
     */
    private interface EcosedResult {

        /**
         * 处理成功结果.
         * @param result 处理成功结果,注意可能为空.
         */
        fun success(result: Any?)

        /**
         * 处理错误结果.
         * @param errorCode 错误代码.
         * @param errorMessage 错误消息,注意可能为空.
         * @param errorDetails 详细信息,注意可能为空.
         */
        fun error(
            errorCode: String,
            errorMessage: String?,
            errorDetails: Any?,
        ): Nothing

        /**
         * 处理对未实现方法的调用.
         */
        fun notImplemented()
    }

    /**
     * 引擎包装器
     */
    private interface EngineWrapper : FlutterPluginProxy {

        /**
         * 执行方法
         * @param channel 插件通道
         * @param method 插件方法
         * @param bundle 传值
         * @return 执行插件方法返回值
         */
        fun <T> execMethodCall(
            channel: String,
            method: String,
            bundle: Bundle?,
        ): T?
    }

    /**
     * 回调
     */
    private interface InvokeWrapper {

        /** 在服务绑定成功时回调 */
        fun onEcosedConnected()

        /** 在服务解绑或意外断开链接时回调 */
        fun onEcosedDisconnected()

        /** 在服务端服务未启动时绑定服务时回调 */
        fun onEcosedDead()

        /** 在未绑定服务状态下调用API时回调 */
        fun onEcosedUnbind()
    }

    /**
     * 生命周期包装器
     */
    private interface LifecycleWrapper : LifecycleOwner, DefaultLifecycleObserver

    /**
     * 服务链接包装器
     */
    private interface ConnectWrapper : ServiceConnection


    /**
     * 服务插件包装器
     */
    private interface DelegateWrapper : ConnectWrapper, LifecycleWrapper {

//        /**
//         * 获取Binder
//         */
//        fun getBinder(intent: Intent): IBinder

        /**
         * 附加代理基本上下文
         */
//        fun attachDelegateBaseContext()
    }

    private interface ITheme {
        /**
         * Compose 主题
         *
         * @param darkTheme 深色模式
         * @param dynamicColor 动态颜色
         * @param content 布局内容
         */
        @Composable
        @UiComposable
        fun EbKitTheme(
            darkTheme: Boolean = isSystemInDarkTheme(),
            dynamicColor: Boolean = true,
            content: @Composable () -> Unit,
        )
    }

    private interface IContent {

        @Composable
        @UiComposable
        fun EbKitContent()
    }

    private interface IColors {
        val purple80: Color
        val purpleGrey80: Color
        val pink80: Color

        val purple40: Color
        val purpleGrey40: Color
        val pink40: Color
    }

    private interface ITypography {
        val typography: Typography
    }

    private interface IBackPressHandler {

    }

    /**
     * 布局预览注解
     */
    @Preview(
        name = "LayoutPreview",
        group = "LayoutPreview",
        device = "id:pixel_9_pro",
        apiLevel = 36,
        showSystemUi = true,
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
        wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    )
    private annotation class SharpPreview

    /**
     * 基本插件
     */
    private abstract class EcosedPlugin : ContextWrapper(null) {

        /** 插件通道 */
        private lateinit var mPluginChannel: PluginChannel

        /** 引擎 */
        private lateinit var mEngine: EngineWrapper

        /** 是否调试模式 */
        private var mDebug: Boolean = false

        /**
         * 附加基本上下文
         */
        override fun attachBaseContext(base: Context?) {
            super.attachBaseContext(base)
        }

        /**
         * 插件添加时执行
         */
        open fun onEcosedAdded(binding: PluginBinding) {
            // 初始化插件通道
            this@EcosedPlugin.mPluginChannel = PluginChannel(
                binding = binding,
                channel = this@EcosedPlugin.channel,
            )
            // 插件附加基本上下文
            this@EcosedPlugin.attachBaseContext(
                base = this@EcosedPlugin.mPluginChannel.getContext()
            )
            // 引擎
            this@EcosedPlugin.mEngine = this@EcosedPlugin.mPluginChannel.getEngine()
            // 获取是否调试模式
            this@EcosedPlugin.mDebug = this@EcosedPlugin.mPluginChannel.isDebug()
            // 设置调用
            this@EcosedPlugin.mPluginChannel.setMethodCallHandler(
                handler = this@EcosedPlugin
            )
        }

        /** 获取插件通道 */
        val getPluginChannel: PluginChannel
            get() = this@EcosedPlugin.mPluginChannel

        /** 需要子类重写的插件标题 */
        abstract val title: String

        /** 需要子类重写的通道名称 */
        abstract val channel: String

        /** 需要子类重写的插件作者 */
        abstract val author: String

        /** 需要子类重写的插件描述 */
        abstract val description: String

        /** 供子类使用的判断调试模式的接口 */
        val isDebug: Boolean
            get() = this@EcosedPlugin.mDebug

        /**
         * 执行方法
         * @param channel 插件通道
         * @param method 插件方法
         * @param bundle 传值
         * @return 执行插件方法返回值
         */
        open fun <T> execPluginMethod(
            channel: String,
            method: String,
            bundle: Bundle?,
        ): T? = this@EcosedPlugin.mEngine.execMethodCall<T>(
            channel = channel,
            method = method,
            bundle = bundle,
        )

        /**
         * 插件调用方法
         */
        open fun onEcosedMethodCall(
            call: EcosedMethodCall,
            result: EcosedResult,
        ) = Unit
    }

    /**
     * 插件绑定器
     */
    private class PluginBinding(
        debug: Boolean,
        context: Context,
        engine: EngineWrapper,
    ) {

        /** 是否调试模式. */
        private val mDebug: Boolean = debug

        /** 应用程序全局上下文. */
        private val mContext: Context = context

        /** 引擎 */
        private val mEngine: EngineWrapper = engine

        /**
         * 是否调试模式.
         * @return Boolean.
         */
        fun isDebug(): Boolean = this@PluginBinding.mDebug

        /**
         * 获取上下文.
         * @return Context.
         */
        fun getContext(): Context = this@PluginBinding.mContext

        /**
         * 获取引擎
         * @return EngineWrapper.
         */
        fun getEngine(): EngineWrapper = this@PluginBinding.mEngine
    }

    /**
     * 插件通信通道
     */
    private class PluginChannel(
        binding: PluginBinding,
        channel: String,
    ) {

        /** 插件绑定器. */
        private var mBinding: PluginBinding = binding

        /** 插件通道. */
        private var mChannel: String = channel

        /** 方法调用处理接口. */
        private var mPlugin: EcosedPlugin? = null

        /** 方法名. */
        private var mMethod: String? = null

        /** 参数Bundle. */
        private var mBundle: Bundle? = null

        /** 返回结果. */
        private var mResult: Any? = null

        /**
         * 设置方法调用.
         * @param handler 执行方法时调用EcosedMethodCallHandler.
         */
        fun setMethodCallHandler(handler: EcosedPlugin) {
            this@PluginChannel.mPlugin = handler
        }

        /**
         * 获取上下文.
         * @return Context.
         */
        fun getContext(): Context = this@PluginChannel.mBinding.getContext()

        /**
         * 是否调试模式.
         * @return Boolean.
         */
        fun isDebug(): Boolean = this@PluginChannel.mBinding.isDebug()

        /**
         * 获取通道.
         * @return 通道名称.
         */
        fun getChannel(): String = this@PluginChannel.mChannel

        /**
         * 获取引擎.
         * @return 引擎.
         */
        fun getEngine(): EngineWrapper = this@PluginChannel.mBinding.getEngine()

        /**
         * 执行方法回调.
         * @param name 通道名称.
         * @param method 方法名称.
         * @return 方法执行后的返回值.
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> execMethodCall(
            name: String,
            method: String?,
            bundle: Bundle?,
        ): T? {
            this@PluginChannel.mMethod = method
            this@PluginChannel.mBundle = bundle
            if (name == this@PluginChannel.mChannel) {
                this@PluginChannel.mPlugin?.onEcosedMethodCall(
                    call = this@PluginChannel.call,
                    result = this@PluginChannel.result,
                )
            }
            return this@PluginChannel.mResult as T?
        }

        /** 用于调用方法的接口. */
        private val call: EcosedMethodCall = object : EcosedMethodCall {

            /**
             * 要调用的方法名.
             */
            override val method: String?
                get() = this@PluginChannel.mMethod

            /**
             * 要传入的参数.
             */
            override val bundle: Bundle?
                get() = this@PluginChannel.mBundle
        }

        /** 方法调用结果回调. */
        private val result: EcosedResult = object : EcosedResult {

            /**
             * 处理成功结果.
             */
            override fun success(result: Any?) {
                this@PluginChannel.mResult = result
            }

            /**
             * 处理错误结果.
             */
            override fun error(
                errorCode: String,
                errorMessage: String?,
                errorDetails: Any?,
            ): Nothing = error(
                message = "错误代码:$errorCode\n错误消息:$errorMessage\n详细信息:$errorDetails"
            )

            /**
             * 处理对未实现方法的调用.
             */
            override fun notImplemented() {
                this@PluginChannel.mResult = null
            }
        }

    }



    /**
     ***********************************************************************************************
     *
     * 路由地址
     *
     ***********************************************************************************************
     */

    /**
     * 路由地址
     *
     * @param label 导航栏标题
     * @param route 路由对象
     * @param icon 导航栏图标
     */
    private data class AppDestination<T>(
        val label: String,
        val route: T,
        val icon: ImageVector,
        val selectedIcon: ImageVector,
    )

    /**
     * 主页路由对象
     *
     * 需要序列化不能私有
     */
    @Serializable
    data object Home

    /**
     * 设置页路由对象
     *
     * 需要序列化不能私有
     */
    @Serializable
    data object Settings

    @Serializable
    data object Installer

    private val mViewFactory: IViewFactory = object : IViewFactory {

        override val getContentFrame: FrameLayout by lazy {
            return@lazy FrameLayout(this@MainActivity)
        }

        override val getContentView: HybridComposeView by lazy {
            return@lazy HybridComposeView(this@MainActivity)
        }

        override val getOverlayView: OverlayView by lazy {
            return@lazy OverlayView(this@MainActivity)
        }

        override val getToolbarView: MaterialToolbar by lazy {
            return@lazy MaterialToolbar(this@MainActivity)
        }

        override val getFlutterView: ViewPager2 by lazy {
            return@lazy ViewPager2(this@MainActivity)
        }
    }

    private val mBackPressHandler: IBackPressHandler = object : IBackPressHandler {

    }

    /**
     ***********************************************************************************************
     *
     * 核心实现
     *
     ***********************************************************************************************
     */

    /** 引擎桥接 */
    private val mEngineBridge: EcosedPlugin = object : EcosedPlugin(), FlutterPluginProxy {

        /** 插件标题 */
        override val title: String
            get() = "EngineBridge"

        /** 插件通道 */
        override val channel: String
            get() = EcosedChannel.BRIDGE_CHANNEL_NAME

        /** 插件作者 */
        override val author: String
            get() = EcosedResources.DEFAULT_AUTHOR

        /** 插件描述 */
        override val description: String
            get() = "FlutterEngine与EcosedEngine通信的的桥梁"

        override fun FlutterPluginProxy.onCreate(activity: Activity) = engineScope {
            return@engineScope this@engineScope.onCreate(activity = activity)
        }

        override fun FlutterPluginProxy.onDestroy() = engineScope {
            return@engineScope this@engineScope.onDestroy()
        }

        override fun FlutterPluginProxy.onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
        ): Boolean = engineScope {
            return@engineScope this@engineScope.onActivityResult(
                requestCode = requestCode,
                resultCode = resultCode,
                data = data,
            )
        }

        override fun FlutterPluginProxy.onMethodCall(
            call: MethodCallProxy,
            result: ResultProxy,
        ) = engineScope {
            return@engineScope this@engineScope.onMethodCall(
                call = call,
                result = result,
            )
        }
    }


    /** 引擎 */
    private val mEcosedEngine: EcosedPlugin = object : EcosedPlugin(), EngineWrapper {

        /** 插件标题 */
        override val title: String
            get() = "EcosedEngine"

        /** 插件通道 */
        override val channel: String
            get() = EcosedChannel.ENGINE_CHANNEL_NAME

        /** 插件作者 */
        override val author: String
            get() = EcosedResources.DEFAULT_AUTHOR

        /** 插件描述 */
        override val description: String
            get() = "Ecosed Engine"

        override fun FlutterPluginProxy.onCreate(activity: Activity) {
            when {
                mPluginList.isNull or mBinding.isNull -> pluginScope(
                    debug = mBaseDebug,
                    context = activity,
                ) { plugins, binding ->
                    // 初始化插件列表.
                    mPluginList = arrayListOf()
                    // 添加所有插件.
                    plugins.forEach { plugin ->
                        plugin.apply {
                            try {
                                this@apply.onEcosedAdded(binding = binding)
                                if (mBaseDebug) Log.d(
                                    TAG,
                                    "插件${this@apply.javaClass.name}已加载",
                                )
                            } catch (exception: Exception) {
                                if (mBaseDebug) Log.e(
                                    TAG,
                                    "插件${this@apply.javaClass.name}添加失败!",
                                    exception,
                                )
                            }
                        }.run {
                            mPluginList?.add(
                                element = this@run
                            )
                            if (mBaseDebug) Log.d(
                                TAG,
                                "插件${this@run.javaClass.name}已添加到插件列表",
                            )
                        }
                    }
                }

                else -> if (mBaseDebug) Log.e(
                    TAG, "请勿重复执行onCreateEngine!"
                ) else Unit
            }
            mActivity = activity
            if (activity is LifecycleOwner) {
                (activity as LifecycleOwner).apply {
                    mLifecycle = lifecycle
                }
            }
            lifecycleScope {
                this@lifecycleScope.lifecycle.addObserver(
                    observer = this@lifecycleScope,
                )
            }
        }

        override fun FlutterPluginProxy.onDestroy() {
            lifecycleScope {
                this@lifecycleScope.lifecycle.removeObserver(this@lifecycleScope)
            }
            mLifecycle = null
            mActivity = null
            when {
                mPluginList.isNotNull or mBinding.isNotNull -> {
                    // 清空插件列表
                    mPluginList = null
                }

                else -> if (mBaseDebug) Log.e(
                    TAG,
                    "请勿重复执行onDestroyEngine!",
                ) else Unit
            }
        }

        override fun FlutterPluginProxy.onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
        ): Boolean {

            return true
        }

        /**
         * 引擎初始化时执行
         */
        override fun onEcosedAdded(binding: PluginBinding): Unit = run {
            super.onEcosedAdded(binding)
            // 设置来自插件的全局调试布尔值
            mFullDebug = this@run.isDebug
        }

        override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
            super.onEcosedMethodCall(call, result)
            when (call.method) {
                EcosedMethod.OPEN_DIALOG_METHOD -> result.success(
                    result = execPluginMethod<Boolean>(
                        channel = EcosedChannel.INVOKE_CHANNEL_NAME,
                        method = EcosedMethod.OPEN_DIALOG_METHOD,
                        bundle = Bundle()
                    )
                )

                EcosedMethod.CLOSE_DIALOG_METHOD -> result.success(
                    result = execPluginMethod<Boolean>(
                        channel = EcosedChannel.INVOKE_CHANNEL_NAME,
                        method = EcosedMethod.CLOSE_DIALOG_METHOD,
                        bundle = Bundle()
                    )
                )

                else -> result.notImplemented()
            }
        }

        /**
         * 方法调用
         * 此方法通过Flutter插件代理类[FlutterPluginProxy]实现
         * 此方法等价与MethodCallHandler的onMethodCall方法
         * 但参数传递是依赖Bundle进行的
         */
        override fun FlutterPluginProxy.onMethodCall(
            call: MethodCallProxy,
            result: ResultProxy,
        ) {
            try {
                // 执行代码并获取执行后的返回值
                execMethodCall<Any>(
                    channel = call.bundleProxy.getString(
                        "channel",
                        EcosedChannel.ENGINE_CHANNEL_NAME,
                    ),
                    method = call.methodProxy,
                    bundle = call.bundleProxy,
                ).apply {
                    // 判断是否为空并提交数据
                    if (this@apply.isNotNull) result.success(
                        resultProxy = this@apply
                    ) else result.notImplemented()
                }
            } catch (e: Exception) {
                // 抛出异常
                result.error(
                    errorCodeProxy = TAG,
                    errorMessageProxy = "engine: onMethodCall",
                    errorDetailsProxy = Log.getStackTraceString(e),
                )
            }
        }

        /**
         * 调用插件代码的方法.
         * @param channel 要调用的插件的通道.
         * @param method 要调用的插件中的方法.
         * @param bundle 通过Bundle传递参数.
         * @return 返回方法执行后的返回值,类型为Any?.
         */
        override fun <T> execMethodCall(
            channel: String,
            method: String,
            bundle: Bundle?,
        ): T? {
            var result: T? = null
            try {
                mPluginList?.forEach { plugin ->
                    plugin.getPluginChannel.let { pluginChannel ->
                        if (pluginChannel.getChannel() == channel) {
                            result = pluginChannel.execMethodCall<T>(
                                name = channel,
                                method = method,
                                bundle = bundle,
                            )
                            if (mBaseDebug) Log.d(
                                TAG,
                                "插件代码调用成功!\n通道名称:${channel}.\n方法名称:${method}.\n返回结果:${result}.",
                            )
                        }
                    }
                }
            } catch (exception: Exception) {
                if (mBaseDebug) {
                    Log.e(
                        TAG,
                        "插件代码调用失败!",
                        exception,
                    )
                }
            }
            return result
        }
    }

    /** 负责与服务通信的客户端 */
    private val mServiceInvoke: EcosedPlugin = object : EcosedPlugin(), InvokeWrapper {

        /** 插件标题 */
        override val title: String
            get() = "ServiceInvoke"

        /** 插件通道 */
        override val channel: String
            get() = EcosedChannel.INVOKE_CHANNEL_NAME

        /** 插件作者 */
        override val author: String
            get() = EcosedResources.DEFAULT_AUTHOR

        /** 插件描述 */
        override val description: String
            get() = "负责与服务通信的服务调用"

        /**
         * 插件添加时执行
         */
//        override fun onEcosedAdded(binding: PluginBinding) = run {
//            super.onEcosedAdded(binding)
//            this@MainActivity.mEcosedServicesIntent = Intent(
//                this@run,
//                MainServices().javaClass,
//            )
//            this@MainActivity.mEcosedServicesIntent.action = EcosedManifest.ACTION
//
//            startService(this@MainActivity.mEcosedServicesIntent)
//            bindEcosed(this@run)
//
//            Toast.makeText(this@run, "client", Toast.LENGTH_SHORT).show()
//        }

        /**
         * 插件方法调用
         */
        override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
            super.onEcosedMethodCall(call, result)
            when (call.method) {
                EcosedMethod.OPEN_DIALOG_METHOD -> result.success(result = invokeMethod {
                    openDialog()
                })

                EcosedMethod.CLOSE_DIALOG_METHOD -> result.success(result = invokeMethod {
                    closeDialog()
                })

                else -> result.notImplemented()
            }
        }

        /**
         * 在服务绑定成功时回调
         */
        override fun onEcosedConnected() {
            Toast.makeText(this, "onEcosedConnected", Toast.LENGTH_SHORT).show()
        }

        /**
         * 在服务解绑或意外断开链接时回调
         */
        override fun onEcosedDisconnected() {
            Toast.makeText(this, "onEcosedDisconnected", Toast.LENGTH_SHORT).show()
        }

        /**
         * 在服务端服务未启动时绑定服务时回调
         */
        override fun onEcosedDead() {
            Toast.makeText(this, "onEcosedDead", Toast.LENGTH_SHORT).show()
        }

        /**
         * 在未绑定服务状态下调用API时回调
         */
        override fun onEcosedUnbind() {
            Toast.makeText(this, "onEcosedUnbind", Toast.LENGTH_SHORT).show()
        }
    }

    /** 服务相当于整个服务类部分无法在大类中实现的方法在此实现并调用 */
    private val mServiceDelegate: EcosedPlugin =
        object : EcosedPlugin(), DelegateWrapper, IViewFactory by mViewFactory {

            /** 插件标题 */
            override val title: String
                get() = "ServiceDelegate"

            /** 插件通道 */
            override val channel: String
                get() = EcosedChannel.DELEGATE_CHANNEL_NAME

            /** 插件作者 */
            override val author: String
                get() = EcosedResources.DEFAULT_AUTHOR

            /** 插件描述 */
            override val description: String
                get() = "服务功能代理, 无实际插件方法实现."

//        override fun attachBaseContext(base: Context?): Unit = base?.run {
//            super.attachBaseContext(base)
////            this@MainActivity.mAppCompatDelegateBaseContext = this@run
//        } ?: Unit

//        /**
//         * 获取Binder
//         * @param intent 意图
//         * @return IBinder
//         */
//        override fun getBinder(intent: Intent): IBinder {
//            return object : IFeOSdk.Stub() {}
//        }

//        override fun attachDelegateBaseContext() {
//            this@MainActivity.mAppCompatDelegate.attachBaseContext2(
//                this@MainActivity.mAppCompatDelegateBaseContext
//            )
//        }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                when (name?.className) {
//                UserService().javaClass.name -> {
//                    if (service.isNotNull and (service?.pingBinder() == true)) {
//                        this@FeOSdk.mIUserService =
//                            IUserService.Stub.asInterface(service)
//                    }
//                    when {
//                        this@FeOSdk.mIUserService.isNotNull -> {
//                            Toast.makeText(this, "mIUserService", Toast.LENGTH_SHORT).show()
//                        }
//
//                        else -> if (this@FeOSdk.mFullDebug) Log.e(
//                            TAG, "UserService接口获取失败 - onServiceConnected"
//                        )
//                    }
//                    when {
//                        this@FeOSdk.mFullDebug -> Log.i(
//                            TAG, "服务已连接 - onServiceConnected"
//                        )
//                    }
//                }

//                MainServices().javaClass.name -> {
//                    if (service.isNotNull and (service?.pingBinder() == true)) {
//                        this@MainActivity.mAIDL = IFeOSdk.Stub.asInterface(service)
//                    }
//                    when {
//                        this@MainActivity.mAIDL.isNotNull -> {
//                            this@MainActivity.mIsBind = true
//                            invokeScope {
//                                onEcosedConnected()
//                            }
//                        }
//
//                        else -> if (this@MainActivity.mFullDebug) Log.e(
//                            TAG, "AIDL接口获取失败 - onServiceConnected"
//                        )
//                    }
//                    when {
//                        mFullDebug -> Log.i(
//                            TAG, "服务已连接 - onServiceConnected"
//                        )
//                    }
//                }

                    else -> {

                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                when (name?.className) {
//                UserService().javaClass.name -> {
//
//                }

//                this@MainActivity.javaClass.name -> {
//                    this@MainActivity.mIsBind = false
//                    this@MainActivity.mAIDL = null
//                    unbindService(this)
//                    invokeScope {
//                        onEcosedDisconnected()
//                    }
//                    if (this@MainActivity.mFullDebug) {
//                        Log.i(TAG, "服务意外断开连接 - onServiceDisconnected")
//                    }
//                }

                    else -> {

                    }
                }

            }

            override fun onBindingDied(name: ComponentName?) {
                super.onBindingDied(name)
                when (name?.className) {
//                UserService().javaClass.name -> {
//
//                }

//                MainActivity.javaClass.name -> {
//
//                }

                    else -> {

                    }
                }
            }

            override fun onNullBinding(name: ComponentName?) {
                super.onNullBinding(name)
                when (name?.className) {
//                UserService().javaClass.name -> {
//
//                }
//
//                MainActivity.javaClass.name -> {
//                    if (mFullDebug) {
//                        Log.e(TAG, "Binder为空 - onNullBinding")
//                    }
//                }

                    else -> {

                    }
                }
            }

//        override fun onBinderReceived() {
//            Toast.makeText(this, "onBinderReceived", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onBinderDead() {
//            Toast.makeText(this, "onBinderDead", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
//            Toast.makeText(this, "onRequestPermissionResult", Toast.LENGTH_SHORT).show()
//        }


            override val lifecycle: Lifecycle
                get() = mLifecycle ?: error(message = "lifecycle is null!")

            /**
             * 活动创建时执行
             */
            @SuppressLint("ClickableViewAccessibility")
            override fun onCreate(owner: LifecycleOwner): Unit = activityScope {
                super.onCreate(owner)
                // 启用全面屏沉浸
                enableEdgeToEdge()

                mFlutterFragment = loadFlutterFragment()

                setSupportActionBar(getToolbarView)

                getFlutterView.apply {
                    isUserInputEnabled = false
                    adapter = FlutterAdapter(
                        activity = this@MainActivity,
                        flutter = mFlutterFragment,
                    )
                }

                getContentFrame.setOnTouchListener(delayHideTouchListener)
                getContentFrame.addView(getContentView)
                getContentView.setContent {
                    ContentScope {
                        EbKitContent()
                    }
                }
            }

            /**
             * 活动启动时执行
             */
            override fun onStart(owner: LifecycleOwner): Unit = activityScope {
                super.onStart(owner)

            }

            /**
             * 活动恢复时执行
             */
            override fun onResume(owner: LifecycleOwner): Unit = activityScope {
                super.onResume(owner)

            }

            /**
             * 活动暂停时执行
             */
            override fun onPause(owner: LifecycleOwner): Unit = activityScope {
                super.onPause(owner)
            }

            /**
             * 活动停止时执行
             */
            override fun onStop(owner: LifecycleOwner): Unit = activityScope {
                super.onStop(owner)

            }

            /**
             * 活动销毁时执行
             */
            override fun onDestroy(owner: LifecycleOwner): Unit = activityScope {
                super.onDestroy(owner)

            }
        }


    /**
     ***********************************************************************************************
     *
     * 自定义 View/ViewGroup
     * 包括 承载JetpackCompose混合开发的视图 和 承载胶囊按钮和角标的叠加层
     *
     ***********************************************************************************************
     */


    /**
     ***********************************************************************************************
     *
     * Jetpack Compose 布局
     *
     ***********************************************************************************************
     */


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class) // Material3
    private val mContent: IContent = object : IContent {


        /**
         * 布局
         */
        @Composable
        override fun EbKitContent() {
            EbKitTheme {
                ActivityMain(
                    factory = mViewFactory
                )
            }
        }

        /**
         * MainActivity布局
         */
        @Composable
        private fun ActivityMain(factory: IViewFactory? = null) {
            var appsLayerVisible by remember { mutableStateOf(value = false) }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                MPScreen(
                    factory = factory,
                    popBackStack = {
                        appsLayerVisible = false
                    },
                )
                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = !appsLayerVisible,
                ) {
                    NavigationRoot(
                        showAppsLayer = {
                            appsLayerVisible = true
                        },
                    )
                }
            }
        }

        @Composable
        private fun NavigationRoot(
            modifier: Modifier = Modifier,
            showAppsLayer: () -> Unit,
        ) {
            val appDestination = arrayListOf(
                AppDestination(
                    label = "Home",
                    route = MainActivity.Home,
                    icon = Icons.Outlined.Home,
                    selectedIcon = Icons.Filled.Home,
                ),
                AppDestination(
                    label = "Settings",
                    route = MainActivity.Settings,
                    icon = Icons.Outlined.Settings,
                    selectedIcon = Icons.Filled.Settings,
                ),
            )
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val customNavSuiteType: NavigationSuiteType = with(
                receiver = currentWindowAdaptiveInfo(),
            ) {
                return@with when (windowSizeClass.windowWidthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> NavigationSuiteType.NavigationBar
                    WindowWidthSizeClass.MEDIUM -> NavigationSuiteType.NavigationRail
                    WindowWidthSizeClass.EXPANDED -> NavigationSuiteType.NavigationDrawer
                    else -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                        adaptiveInfo = this@with
                    )
                }
            }
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    appDestination.forEach { destination ->
                        val isCurrent: Boolean = currentDestination?.hierarchy?.any {
                            return@any it.hasRoute(route = destination.route::class)
                        } == true
                        item(
                            icon = {
                                Icon(
                                    imageVector = if (isCurrent) {
                                        destination.selectedIcon
                                    } else {
                                        destination.icon
                                    },
                                    contentDescription = destination.label,
                                )
                            },
                            modifier = Modifier.wrapContentSize(),
                            label = {
                                Text(text = destination.label)
                            },
                            selected = isCurrent,
                            onClick = {
                                navController.navigate(
                                    route = destination.route,
                                ) {
                                    popUpTo(
                                        id = navController.graph.findStartDestination().id,
                                    ) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            alwaysShowLabel = false,
                        )
                    }
                },
                modifier = modifier.fillMaxSize(),
                layoutType = customNavSuiteType,
            ) {
                NavHost(
                    navController = navController,
                    startDestination = MainActivity.Home,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable<MainActivity.Home> {
                        HomeDestination(
                            navController = navController,
                            showAppsLayer = showAppsLayer,
                        )
                    }
                    composable<MainActivity.Settings> {
                        SettingsDestination()
                    }
                    composable<Installer> {
                        InstallerDestination(navController = navController)
                    }

                }
            }
        }

        @Composable
        private fun HomeDestination(
            modifier: Modifier = Modifier,
            navController: NavController,
            showAppsLayer: () -> Unit,
        ) {

            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                state = rememberTopAppBarState()
            )
            var expanded: Boolean by remember {
                mutableStateOf(value = false)
            }
            val scroll = rememberScrollState()
            val context: Context = LocalContext.current
            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection,
                    ),
                topBar = {
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text(text = stringResource(id = R.string.app_name))
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    navController.navigate(route = Installer)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.InstallMobile,
                                    contentDescription = null,
                                )
                            }
                            IconButton(
                                onClick = {
                                    expanded = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.MoreVert,
                                    contentDescription = null,
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = {
                                        expanded = false
                                    },
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = "Settings")
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Outlined.Settings,
                                                contentDescription = null,
                                            )
                                        },
                                        onClick = {
                                            navController.navigate(
                                                route = MainActivity.Settings
                                            ) {
                                                popUpTo(
                                                    id = navController.graph.findStartDestination().id
                                                ) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }.also {
                                                expanded = !expanded
                                            }
                                        },
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = "About")
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Outlined.Info,
                                                contentDescription = null,
                                            )
                                        },
                                        onClick = {
                                            expanded = !expanded
                                        },
                                    )
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                        .verticalScroll(state = scroll),
                ) {
                    StateCard(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 8.dp,
                        ),
                        style = StateCardStyle.Normal,
                        navToInstaller = {
                            navController.navigate(
                                route = Installer,
                            ) {
                                popUpTo(
                                    id = navController.graph.findStartDestination().id,
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        showAppsLayer = showAppsLayer,
                    )

                }
            }
        }

        @Composable
        private fun InstallerDestination(
            modifier: Modifier = Modifier,
            navController: NavController,
        ) {
            Scaffold(modifier = modifier.fillMaxSize(), topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Installer")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                )
            }) { innerPadding ->
                Box(modifier = Modifier.padding(paddingValues = innerPadding)) {

                }
            }
        }


        @Composable
        private fun SettingsDestination() {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                rememberTopAppBarState()
            )
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection
                    ),
                topBar = {
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text("Settings")
                        },
                        actions = {},
                        colors = TopAppBarDefaults.topAppBarColors(),
                        scrollBehavior = scrollBehavior
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding),
                    contentAlignment = Alignment.Center
                ) {


                }
            }
        }


    }

    /**
     ***********************************************************************************************
     *
     * 私有函数
     *
     ***********************************************************************************************
     */




    /**
     * 调用方法
     */
    private inline fun invokeMethod(block: () -> Unit): Boolean {
        try {
            block.invoke()
            return true
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            return false
        }
    }

    /**
     * 切换工具栏显示状态
     */
    private fun toggle() {
        if (isVisible) hide() else show()
    }

    /**
     * 隐藏工具栏
     */
    private fun hide() {
        getActionBar()?.hide()
        isVisible = false
        hideHandler.removeCallbacks(showPart2Runnable)
    }

    /**
     * 显示工具栏
     */
    private fun show() {
        isVisible = true
        hideHandler.postDelayed(
            showPart2Runnable, UI_ANIMATOR_DELAY.toLong()
        )
    }

    /**
     * 延时隐藏
     */
    private fun delayedHide() {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(
            hideRunnable, AUTO_HIDE_DELAY_MILLIS.toLong()
        )
    }


    /**
     * 初始化
     */
    private fun init(onCreate: () -> Unit) = activityScope {
        // 初始化Delegate
//        initDelegate()
        // 初始化工具栏状态
        isVisible = true
//        initTheme()
//        // 判断Activity是否为AppCompatActivity
//        if (this@activityScope.isNotAppCompat) delegateScope {
//            // 为了保证接下来的Delegate调用，如果不是需要设置AppCompat主题
//
//            // 调用Delegate onCreate函数
//            onCreate()
//        }
        // 初始化用户界面
        initUi()
    }

//    /**
//     * 初始化委托
//     */
//    private fun initDelegate(): Unit = activityScope {
//        // 初始化Delegate
//        this@MainActivity.mAppCompatDelegate = if (true) {
//            (this@activityScope as AppCompatActivity).run {
//                return@run this@run.delegate
//            }
//        } else {
//            appCompatScope {
//                return@appCompatScope AppCompatDelegate.create(
//                    this@activityScope,
//                    this@appCompatScope,
//                )
//            }
//        }
//        // 附加Delegate基本上下文
////        if (false) serviceScope {
////            attachDelegateBaseContext()
////        }
//    }

//    /**
//     * 初始化主题
//     */
//    private fun initTheme(): Unit = activityScope {
//        val attributes: TypedArray = obtainStyledAttributes(
//            androidx.appcompat.R.styleable.AppCompatTheme
//        )
//        if (!attributes.hasValue(androidx.appcompat.R.styleable.AppCompatTheme_windowActionBar)) {
//            attributes.recycle()
//            setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
//        }
//    }

    /**
     * 初始化用户界面
     */
    private fun initUi(): Unit = activityScope {
        // 初始化工具栏
        Toolbar(this@activityScope).apply {
            this@apply.navigationIcon = ContextCompat.getDrawable(
                this@activityScope,
                R.drawable.baseline_menu_24,
            )
            //  subtitle = EcosedResources.PROJECT_NAME
            this@apply.setNavigationOnClickListener { view ->

            }
            mToolbar = this@apply
        }
        // 初始化对话框
        AlertDialog.Builder(this@activityScope).apply {
            setTitle("Debug Menu (Native)")
            setItems(
                arrayOf(
                    "Launch Shizuku", "Launch microG", "Request Permissions"
                )
            ) { dialog, which ->
                when (which) {
//                    0 -> if (AppUtils.isAppInstalled(EcosedManifest.SHIZUKU_PACKAGE)) {
//                        AppUtils.launchApp(EcosedManifest.SHIZUKU_PACKAGE)
//                    } else {
//                        // 跳转安装
//                    }
                    0 -> {}

                    1 -> {
                        // 判断是否支持谷歌基础服务
                        if (isSupportGMS()) {
                            // 判断如果有启动图标直接打开 - 针对microG
                            if (IntentUtils.getLaunchAppIntent(EcosedManifest.GMS_PACKAGE).isNotNull) {
                                AppUtils.launchApp(EcosedManifest.GMS_PACKAGE)
                            } else {
                                // 如果没有启动图标使用包名和类名启动 - 针对谷歌GMS
                                val intent = IntentUtils.getComponentIntent(
                                    EcosedManifest.GMS_PACKAGE,
                                    EcosedManifest.GMS_CLASS,
                                )
                                if (IntentUtils.isIntentAvailable(intent)) {
                                    try {
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        // 启动失败
                                    }
                                } else {
                                    // 意图不可用
                                }
                            }
                        } else {
                            // 跳转安装microG
                        }

                        //gms(this@activityUnit)

                    }

                    2 -> {
                        requestPermissions()
                    }

                    else -> {}
                }
            }
            setView(mToolbar)
            setPositiveButton(EcosedResources.POSITIVE_BUTTON_STRING) { dialog, which -> }
            mDebugDialog = create()
        }

//        // 设置操作栏
//        delegateScope {
//            // 仅在使用Flutter的Activity时设置ActionBar,防止影响混合应用的界面.
////            if (this@activityScope.isFlutter) {
////                setSupportActionBar(mToolbar)
////            } else {
////                mToolbar.setTitle(AppUtils.getAppName())
////            }
//
//        }
        // 设置根视图触摸事件

    }

    /**
     * 打开对话框
     */
    private fun openDialog() {
        if (!mDebugDialog.isShowing) {
            mDebugDialog.show()
        }
    }

    /**
     * 关闭对话框
     */
    private fun closeDialog() {
        if (mDebugDialog.isShowing) {
            mDebugDialog.dismiss()
        }
    }

    /**
     * 判断是否支持谷歌基础服务
     */
    private fun isSupportGMS(): Boolean = activityScope {
//        return@activityScope if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
//                this@activityScope
//            ) == ConnectionResult.SUCCESS
//        ) true else AppUtils.isAppInstalled(
//            EcosedManifest.GMS_PACKAGE
//        )
        return@activityScope false
    }

    /**
     * 请求权限
     */
    private fun requestPermissions() {
        try {
            PermissionUtils.permission(EcosedManifest.FAKE_PACKAGE_SIGNATURE).request()
        } catch (e: IllegalStateException) {

        }
    }

    /**
     * 获取根视图
     */
    private fun findRootView(): View = activityScope {
        return@activityScope this.window.decorView
    }

    /**
     * 绑定服务
     * @param context 上下文
     */
    private fun bindEcosed(context: Context) = connectScope {
        try {
            if (!mIsBind) {
                context.bindService(
                    mEcosedServicesIntent,
                    this@connectScope,
                    BIND_AUTO_CREATE,
                ).let { bind ->
                    invokeScope {
                        if (!bind) onEcosedDead()
                    }
                }
            }
        } catch (e: Exception) {
            if (mFullDebug) {
                Log.e(TAG, "bindEcosed", e)
            }
        }
    }

//    /**
//     * 解绑服务
//     * @param context 上下文
//     */
//    private fun unbindEcosed(context: Context) = connectScope {
//        try {
//            if (this@MainActivity.mIsBind) {
//                context.unbindService(
//                    this@connectScope
//                ).run {
//                    this@MainActivity.mIsBind = false
//                    this@MainActivity.mAIDL = null
//                    invokeScope {
//                        onEcosedDisconnected()
//                    }
//                    if (this@MainActivity.mFullDebug) {
//                        Log.i(TAG, "服务已断开连接 - onServiceDisconnected")
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            if (this@MainActivity.mFullDebug) {
//                Log.e(TAG, "unbindEcosed", e)
//            }
//        }
//    }

    private fun gms(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.setPackage(EcosedManifest.GMS_PACKAGE)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.w(TAG, "MAIN activity is not DEFAULT. Trying to resolve instead.")
                intent.setClassName(
                    EcosedManifest.GMS_PACKAGE,
                    packageManager.resolveActivity(intent, 0)!!.activityInfo.name
                )
                context.startActivity(intent)
            }
            Toast.makeText(context, "toast_installed", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.w(TAG, "Failed launching microG Settings", e)
            Toast.makeText(context, "toast_not_installed", Toast.LENGTH_LONG).show()
        }
    }


    /**
     * 覆写函数空间
     *
     * 用于 [MainActivity] 生命周期函数通过桥接单元启动引擎
     *
     * @param parent 调用父类super函数, 带有 [T] 类型 Implicit Receiver, 返回值 [R]
     * @param scope 调用桥接调用单元函数返回 [S] , 带有 [T] 类型 Implicit Receiver, [R] 类型参数, 返回值 [S]
     * @param block 生命周期函数体, 带有 [S] 类型 Implicit Receiver, [T] 和 [R] 类型参数, 返回值 [R]
     * @return [R] 返回值
     */
    private fun <Current, Receiver, Scope, Result> Current.onScope(
        receiver: Current.() -> Receiver,
        scope: Receiver.() -> Scope,
        throws: Current.(Any) -> Nothing,
        block: Scope.(Receiver) -> Result,
    ): Result {
        try {
            return block.invoke(scope(receiver()), receiver())
        } catch (exception: Exception) {
            throws(exception)
        }
    }

    private fun <R> myselfScope(
        block: Activity.() -> R,
    ): R = block.invoke(
        me.run {
            return@run when (this@run) {
                is Activity -> this@run
                else -> error(
                    message = "??? me is not activity ???",
                )
            }
        },
    )


    /**
     * 框架调用单元
     * Flutter插件调用框架
     * @param block Flutter插件代理单元
     * @return content 返回值
     */
    private fun <R> bridgeScope(
        block: FlutterPluginProxy.() -> R,
    ): R = block.invoke(
        mEngineBridge.run {
            return@run when (this@run) {
                is FlutterPluginProxy -> this@run
                else -> error(
                    message = "引擎桥接未实现插件代理方法",
                )
            }
        },
    )

    /**
     * 引擎调用单元
     * 框架调用引擎
     * @param block 引擎包装器单元
     * @return content 返回值
     */
    private fun <R> MainActivity.engineScope(
        block: EngineWrapper.() -> R,
    ): R = block.invoke(
        mEcosedEngine.run {
            return@run when (this@run) {
                is EngineWrapper -> this@run
                else -> error(
                    message = "引擎未实现引擎包装器方法",
                )
            }
        },
    )

    /**
     * 生命周期调用单元
     * 调用生命周期所有者和生命周期观察者
     * @param block 生命周期包装器
     * @return content 返回值
     */
    private fun <R> MainActivity.lifecycleScope(
        block: LifecycleWrapper.() -> R,
    ): R = block.invoke(
        mServiceDelegate.run {
            return@run when (this@run) {
                is LifecycleWrapper -> this@run
                else -> error(
                    message = "服务代理未实现生命周期包装器方法",
                )
            }
        },
    )

    /**
     * 插件调用单元
     * 插件初始化
     * @param context 上下文
     * @param block 插件列表单元, 插件绑定器
     * @return content 返回值
     */
    private fun <R> MainActivity.pluginScope(
        context: Context,
        debug: Boolean,
        block: (
            ArrayList<EcosedPlugin>,
            PluginBinding,
        ) -> R,
    ): R = block.invoke(
        arrayListOf(
            mEngineBridge,
            mEcosedEngine,
            mServiceInvoke,
            mServiceDelegate,
        ),
        PluginBinding(
            debug = debug,
            context = context,
            engine = mEcosedEngine.run {
                return@run when (this@run) {
                    is EngineWrapper -> this@run
                    else -> error(
                        message = "引擎未实现引擎包装器方法",
                    )
                }
            },
        ),
    )

    /**
     * 客户端回调调用单元
     * 绑定解绑调用客户端回调
     * @param block 客户端回调单元
     * @return content 返回值
     */
    private fun <R> MainActivity.invokeScope(
        block: InvokeWrapper.() -> R,
    ): R = block.invoke(
        mServiceInvoke.run {
            return@run when (this@run) {
                is InvokeWrapper -> this@run
                else -> error(
                    message = "服务调用插件未实现客户端包装器方法",
                )
            }
        },
    )

    /**
     * 服务调用单元
     * 服务与服务嗲用
     * @param block 服务
     * @return content 返回值
     */
    private fun <R> MainActivity.serviceScope(
        block: DelegateWrapper.() -> R,
    ): R = block.invoke(
        mServiceDelegate.run {
            return@run when (this@run) {
                is DelegateWrapper -> this@run
                else -> error(
                    message = "服务代理未实现服务代理包装器方法",
                )
            }
        },
    )

    /**
     * 服务连接器调用单元
     * 调用服务连接包装器
     * @param block 服务链接包装器
     * @return content 返回值
     */
    private fun <R> MainActivity.connectScope(
        block: ConnectWrapper.() -> R,
    ): R = block.invoke(
        mServiceDelegate.run {
            return@run when (this@run) {
                is ConnectWrapper -> this@run
                else -> error(
                    message = "服务代理未实现连接包装器方法",
                )
            }
        },
    )

    /**
     * Activity上下文调用单元
     * Activity生命周期观察者通过此调用单元执行基于Activity上下文的代码
     * @param block 内容
     * @return content 返回值
     */
    private fun <R> MainActivity.activityScope(
        block: Activity.() -> R,
    ): R = block.invoke(
        mActivity ?: error(
            message = "activity is null"
        ),
    )

    /**
     * Compose布局调用单元
     *
     * [Preview] 预览函数调用布局
     *
     * @param block 布局代理单元
     * @return content 返回值
     */
    @Composable
    private fun MainActivity.ContentScope(
        block: @Composable IContent.() -> Unit,
    ) = block.invoke(mContent)

    private fun ByteArray.digest(algorithm: String): ByteArray =
        MessageDigest.getInstance(algorithm).digest(this)

    private fun PackageInfo.getSignature(): String? {
        val apkSigners = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            this@getSignature.signingInfo?.apkContentsSigners
        } else {
            this@getSignature.signatures
        }

        return apkSigners?.firstOrNull()?.toByteArray()?.digest(
            algorithm = "sha256",
        )?.toHexString(
            format = HexFormat.UpperCase,
        )
    }

    /**
     * 扩展函数判断是否为空
     */
    private inline val Any?.isNull: Boolean
        get() = this@isNull == null

    /**
     * 扩展函数判断是否为空
     */
    private inline val Any?.isNotNull: Boolean
        get() = this@isNotNull != null

    /**
     * 布局预览
     */
    @Composable
    @SharpPreview
    private fun Preview() {
        ContentScope {
            EbKitContent()
        }
    }

    /**
     * 资源
     */
    private object EcosedResources {

        /** 开发者 */
        const val DEFAULT_AUTHOR: String = "wyq0918dev"

        /** 确定按钮文本 */
        const val POSITIVE_BUTTON_STRING: String = "确定"

    }

    /**
     * 清单
     */
    private object EcosedManifest {

        /** 谷歌基础服务包名 */
        const val GMS_PACKAGE: String = "com.google.android.gms"

        const val GMS_CLASS: String = "com.google.android.gms.app.settings.GoogleSettingsLink"

        /** 签名伪装权限 */
        const val FAKE_PACKAGE_SIGNATURE: String = "android.permission.FAKE_PACKAGE_SIGNATURE"
    }

    /**
     * 通道
     */
    private object EcosedChannel {
        /** Flutter插件通道名称 */
        const val FLUTTER_CHANNEL_NAME: String = "flutter_ecosed"

        /** 引擎桥梁插件 */
        const val BRIDGE_CHANNEL_NAME: String = "ecosed_bridge"

        /** 引擎 */
        const val ENGINE_CHANNEL_NAME: String = "ecosed_engine"

        /** 服务调用插件 */
        const val INVOKE_CHANNEL_NAME: String = "ecosed_invoke"

        /** 服务代理插件 */
        const val DELEGATE_CHANNEL_NAME: String = "ecosed_delegate"
    }

    /**
     * 方法
     */
    private object EcosedMethod {

        /** 打开对话框 */
        const val OPEN_DIALOG_METHOD: String = "openDialog"

        /** 关闭对话框 */
        const val CLOSE_DIALOG_METHOD: String = "closeDialog"
    }

    /**
     * 构建器(伴生对象)
     */
    companion object {

        /** 用于打印日志的标签 */
        private const val TAG: String = "MainActivity"

        /** 操作栏是否应该在[AUTO_HIDE_DELAY_MILLIS]毫秒后自动隐藏。*/
        private const val AUTO_HIDE: Boolean = false

        /** 如果设置了[AUTO_HIDE]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        private const val AUTO_HIDE_DELAY_MILLIS: Int = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        private const val UI_ANIMATOR_DELAY: Int = 300


    }
}


package io.ebkit.app

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import androidx.window.core.layout.WindowWidthSizeClass
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.PermissionUtils
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.JumpParameter
import io.ebkit.app.MainActivity.Companion.AUTO_HIDE
import io.ebkit.app.MainActivity.Companion.AUTO_HIDE_DELAY_MILLIS
import kotlinx.serialization.Serializable
import org.lsposed.hiddenapibypass.HiddenApiBypass
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : BaseActivity() {

    /** 实时更新的安全区值 */
    private var currentSafeInsets = Insets.NONE


    private val bannerDistanceOriginPointLength = 55.toDp
    private val bannerWidth = 16.toDp

    /** 边角横幅画笔 */
    private val sdkBannerPaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = "#A0B71C1C".toColorInt()
        style = Paint.Style.FILL
    }
    private val debugBannerPaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = "#A0B71C1C".toColorInt()
        style = Paint.Style.FILL
    }
    private val capsuleFillPaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = "#99FFFFFF".toColorInt() // 99FFFFFF
        style = Paint.Style.FILL
    }
    private val capsuleStrokePaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = "#19000000".toColorInt()
        style = Paint.Style.STROKE
        strokeWidth = 1.toDp.toFloat()
    }
    private val capsuleDividerPaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = "#19000000".toColorInt()
        style = Paint.Style.STROKE
        strokeWidth = 1.toDp.toFloat()
    }


    /**
     * 横幅文字画笔
     */
    private val sdkBannerTextPaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = android.graphics.Color.WHITE
        textSize = 10.toSp.toFloat()
        style = Paint.Style.FILL
        textAlign = Paint.Align.LEFT
    }
    private val debugBannerTextPaint: Paint = Paint(
        Paint.ANTI_ALIAS_FLAG,
    ).apply {
        color = android.graphics.Color.WHITE
        textSize = 10.toSp.toFloat()
        style = Paint.Style.FILL
        textAlign = Paint.Align.LEFT
    }


    /**
     * 横幅Path
     */
    private val sdkBannerPath: Path = Path()
    private val debugBannerPath: Path = Path()
    private val capsuleFillPath: Path = Path()
    private val capsuleStrokePath: Path = Path()

    private var viewWidth = 0
    private var viewHeight = 0

    /**
     * 真正绘制的文字,如果bannerText过长,可能会被裁剪
     */
    private var sdkBannerText: String = "ECOSED"
    private var debugBannerText: String = "DEBUG"

    private val sdkBannerPointList by lazy {
        mutableListOf<Point>()
    }

    private val debugBannerPointList by lazy {
        mutableListOf<Point>()
    }


    /** 胶囊宽度 */
    private val capsuleWidth = 86.toDp

    /** 胶囊高度 */
    private val capsuleHeight = 32.toDp

    /** 胶囊又边距 */
    private val capsuleRightPadding = 16.toDp

    /** 胶囊顶部边距 */
    private val capsuleTopPadding = ((65.toDp) - capsuleHeight) / 2

    /** 胶囊圆角半径 */
    private var capsuleRadius = 20.toDp.toFloat()

    /* 是否显示调试信息 **/
    private val show: Boolean = BuildConfig.DEBUG


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
            false
        }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }


    override fun resetContentView(): View = mContentFrame

    override fun initViews() {
        setLifeCircleListener(mLifecycleDelegate)
    }

    override fun initDatas(parameter: JumpParameter?) {

    }

    override fun setEvents() {

    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        (mContentFrame as ViewGroup).addView(view, params)
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


    private interface IViewFactory {
        val getContentView: View
        val getOverlayView: View
        val getMenuButton: View
        val getCloseButton: View

        val getFillMaxSize: ViewGroup.LayoutParams
        val getWrapContentSize: ViewGroup.LayoutParams
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

    private data class Point(
        val x: Float,
        val y: Float,
    )

    data class MiniProgramItem(
        /**
         * 名称
         */
        val title: String,
        /**
         * 图标
         */
        val icon: String,
    )

    enum class AppItemStyle {
        Image, Icon,
    }

    private val miniProgramList: ArrayList<MiniProgramItem> = arrayListOf<MiniProgramItem>(
        MiniProgramItem(
            title = "饿了么",
            icon = "https://img0.baidu.com/it/u=2625005847,2716895016&fm=253&fmt=auto&app=138&f=JPEG?w=200&h=200"
        ),
        MiniProgramItem(
            title = "美图",
            icon = "https://img1.baidu.com/it/u=1752963805,4078506746&fm=253&fmt=auto&app=138&f=JPEG?w=200&h=200"
        ),
        MiniProgramItem(
            title = "滴滴",
            icon = "https://img0.baidu.com/it/u=1068101613,1323308017&fm=253&fmt=auto&app=138&f=PNG?w=200&h=200"
        ),
        MiniProgramItem(
            title = "青橘单车",
            icon = "https://img0.baidu.com/it/u=195120191,2939897897&fm=253&fmt=auto&app=138&f=PNG?w=190&h=190"
        ),
        MiniProgramItem(
            title = "斗地主",
            icon = "https://img2.baidu.com/it/u=926635057,1451495262&fm=253&fmt=auto&app=138&f=PNG?w=190&h=190"
        ),
        MiniProgramItem(
            title = "羊城通",
            icon = "https://img2.baidu.com/it/u=2751300851,4181594410&fm=253&fmt=auto&app=138&f=JPEG?w=200&h=200"
        ),
        MiniProgramItem(
            title = "美图秀秀",
            icon = "https://img1.baidu.com/it/u=417359459,147216874&fm=253&fmt=auto&app=138&f=PNG?w=200&h=200"
        ),
        MiniProgramItem(
            title = "拼多多",
            icon = "https://img2.baidu.com/it/u=620052409,134315960&fm=253&fmt=auto&app=138&f=PNG?w=190&h=190"
        ),
    )

    private val mineMiniProgramList: ArrayList<MiniProgramItem> = arrayListOf<MiniProgramItem>(
        MiniProgramItem(
            title = "羊城通",
            icon = "https://img2.baidu.com/it/u=2751300851,4181594410&fm=253&fmt=auto&app=138&f=JPEG?w=200&h=200"
        ),
        MiniProgramItem(
            title = "青桔单车",
            icon = "https://img0.baidu.com/it/u=195120191,2939897897&fm=253&fmt=auto&app=138&f=PNG?w=190&h=190"
        ),
    )

    private val mOutBoundSpringStiff = 150f
    private val mOutBoundSpringDamp = 0.86f

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

    private val mViewFactory: IViewFactory = object : IViewFactory {
        override val getContentView: View
            get() = mHybridCompose
        override val getOverlayView: View
            get() = mComposeOverlay
        override val getMenuButton: View
            get() = mMenuButton
        override val getCloseButton: View
            get() = mCloseButton

        override val getFillMaxSize: ViewGroup.LayoutParams by lazy {
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
        }
        override val getWrapContentSize: ViewGroup.LayoutParams by lazy {
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
            )
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
            override fun onCreate(owner: LifecycleOwner): Unit = activityScope {
                super.onCreate(owner)

                // 启用全面屏沉浸
                enableEdgeToEdge()

                mContentFrame.setOnTouchListener(delayHideTouchListener)
                setContentView(view = getContentView, params = getFillMaxSize)
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

    private val mContentFrame: View by lazy {
        return@lazy object : FrameLayout(me) {

        }
    }

    /** Compose Hybrid  */
    private val mHybridCompose: View by lazy {
        return@lazy object : AbstractComposeView(
            context = this@MainActivity,
        ), IContent by mContent {

            /**
             * 初始化
             */
            init {
                // 必须加此代码否则不符合预期
                consumeWindowInsets = false
            }

            /**
             * 布局
             */
            @Composable
            override fun Content() {
                EbKitContent()
            }

            /**
             * 无障碍
             */
            override fun getAccessibilityClassName(): CharSequence {
                return this@MainActivity.toString()
            }
        }
    }

    private val mComposeOverlay: View by lazy {
        return@lazy object : FrameLayout(
            this@MainActivity,
        ), IBackPressHandler by mBackPressHandler, IViewFactory by mViewFactory {

            /**
             * 初始化
             */
            init {
                ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
                    currentSafeInsets = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars(),
                    )
                    setPadding(
                        currentSafeInsets.left,
                        currentSafeInsets.top,
                        currentSafeInsets.right,
                        currentSafeInsets.bottom
                    ) // 立即更新布局和绘制
                    invalidate()
                    requestLayout()
                    return@setOnApplyWindowInsetsListener insets // 保持事件传递（不影响 Compose）
                } // 启用窗口插入监听（兼容 API 24+）
                ViewCompat.requestApplyInsets(this) // 初始请求插入计算
                addView(getMenuButton, getWrapContentSize) // 添加菜单按钮
                addView(getCloseButton, getWrapContentSize) // 添加关闭按钮
                setWillNotDraw(false) // 启用内容绘制
            }

            override fun onViewAdded(child: View?) {
                super.onViewAdded(child)
                child?.let { childView ->
                    when (childView) {
                        getMenuButton -> (childView as AppCompatImageButton).apply {
                            setBackgroundColor(Color.Transparent.toArgb())
                            setImageResource(R.drawable.baseline_more_horiz_24)
                            setOnClickListener {
                                Toast.makeText(this@MainActivity, "mMenuButton", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        getCloseButton -> (childView as AppCompatImageButton).apply {
                            setBackgroundColor(Color.Transparent.toArgb())
                            setImageResource(R.drawable.baseline_close_24)
                            setOnClickListener {
                                Toast.makeText(
                                    this@MainActivity, "mCloseButton", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        else -> error(message = "unknown instance")
                    }
                }
            }

            /**
             * 布局
             *
             * @param changed 视图是否发生了变化
             * @param left 视图的左侧坐标
             * @param top 视图的顶部坐标
             * @param right 视图的右侧坐标
             * @param bottom 视图的底部坐标
             */
            override fun onLayout(
                changed: Boolean,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
            ) {
                if (changed) {
                    // 起始位置偏移量，用于放置第一个按钮
                    var leftOffset = 0
                    // 遍历子视图
                    for (index in 0 until childCount.let { count ->
                        // 子View列表
                        val childList: ArrayList<View> = arrayListOf(
                            getMenuButton,
                            getCloseButton,
                        )
                        // 返回子View数量
                        return@let if (count <= childList.size) {
                            count
                        } else error(
                            message = "childCount > 2",
                        )
                    }) {
                        when (index) {
                            // 仅布局前两个子视图
                            0, 1 -> {
                                // 布局子视图
                                getChildAt(index).layout(
                                    // 按钮的左侧坐标
                                    // 视图宽度 - 视图右边距 - 胶囊按钮右边距 - 胶囊按钮宽度 + 左侧偏移量
                                    viewWidth - paddingRight - capsuleRightPadding - capsuleWidth + leftOffset,
                                    // 按钮的顶部坐标
                                    // 胶囊按钮顶部边距 + 视图顶部边距
                                    capsuleTopPadding + paddingTop,
                                    // 按钮的右侧坐标
                                    // 视图宽度 - 视图右边距 - 胶囊按钮右边距 - 胶囊按钮宽度 + 左侧偏移量 + 胶囊按钮宽度的一半
                                    viewWidth - paddingRight - capsuleRightPadding - capsuleWidth + leftOffset + capsuleWidth / 2,
                                    // 按钮的底部坐标
                                    // 胶囊按钮顶部边距 + 胶囊按钮高度 + 视图顶部边距
                                    capsuleTopPadding + capsuleHeight + paddingTop,
                                )
                                // 更新下一个按钮的起始位置
                                leftOffset += capsuleWidth / 2
                            }
                            // 跳出循环,拒绝其他子视图布局
                            else -> continue
                        }
                    }
                    // 布局结束后重置起始位置偏移量
                    leftOffset = 0
                }
            }

            /**
             * 绘制
             *
             * @param canvas 画布对象
             */
            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                // 绘制胶囊按钮背景
                drawCapsuleFill(canvas)
                drawCapsuleStroke(canvas)
                drawCapsuleDivider(canvas)
            }

            /**
             * 绘制前景内容
             *
             * @param canvas 画布对象
             */
            override fun onDrawForeground(canvas: Canvas) {
                super.onDrawForeground(canvas)
                if (show) {
                    // 绘制SDK标识角标
                    val sdkPointList = generatorSdkPointByPosition()
                    drawSdkBanner(canvas, sdkPointList)
                    drawSdkText(canvas, sdkPointList)
                    // 绘制调试模式标识角标
                    val debugPointList = generatorDebugPointByPosition()
                    drawDebugBanner(canvas, debugPointList)
                    drawDebugText(canvas, debugPointList)
                    // 绘制版本水印
//                    drawActivationText(canvas)
                }
            }

            /**
             * 设置尺寸
             *
             * @param width 宽度
             * @param height 高度
             * @param oldWidth 旧宽度
             * @param oldHeight 旧高度
             */
            override fun onSizeChanged(
                width: Int,
                height: Int,
                oldWidth: Int,
                oldHeight: Int,
            ) {
                super.onSizeChanged(
                    width,
                    height,
                    oldWidth,
                    oldHeight,
                )
                viewWidth = width
                viewHeight = height
            }

            /**
             * 禁用触摸事件拦截
             *
             * @param ev 触摸事件
             * @return 是否拦截事件
             */
            override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
                return false
            }
        }
    }

    private val mMenuButton: View by lazy {
        return@lazy object : AppCompatImageButton(this@MainActivity) {

            init {

            }
        }
    }

    private val mCloseButton: View by lazy {
        return@lazy object : AppCompatImageButton(this@MainActivity) {


            init {

            }
        }
    }

    /**
     ***********************************************************************************************
     *
     * Jetpack Compose 布局
     *
     ***********************************************************************************************
     */

    private val mColors: IColors = object : IColors {
        override val purple80: Color = Color(0xFFD0BCFF)
        override val purpleGrey80: Color = Color(0xFFCCC2DC)
        override val pink80: Color = Color(0xFFEFB8C8)

        override val purple40: Color = Color(0xFF6650a4)
        override val purpleGrey40: Color = Color(0xFF625b71)
        override val pink40: Color = Color(0xFF7D5260)
    }

    private val mTypography: ITypography = object : ITypography {

        override val typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            )
        )
    }

    private val mTheme: ITheme = object : ITheme, IColors by mColors, ITypography by mTypography {

        private val darkColorScheme = darkColorScheme(
            primary = purple80,
            secondary = purpleGrey80,
            tertiary = pink80,
        )

        private val lightColorScheme = lightColorScheme(
            primary = purple40,
            secondary = purpleGrey40,
            tertiary = pink40,
        )

        /**
         * 主题
         */
        @Composable
        override fun EbKitTheme(
            darkTheme: Boolean,
            dynamicColor: Boolean,
            content: @Composable () -> Unit,
        ) {
            val colorScheme = when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    if (darkTheme) {
                        dynamicDarkColorScheme(context)
                    } else {
                        dynamicLightColorScheme(context)
                    }
                }

                darkTheme -> darkColorScheme
                else -> lightColorScheme
            }

            MaterialTheme(
                colorScheme = colorScheme,
                typography = typography,
                content = content,
            )
        }
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class) // Material3
    private val mContent: IContent = object : IContent, ITheme by mTheme {

        /**
         * 获取胶囊按钮右填充
         */
        @Composable
        private fun rememberCapsulePadding(): PaddingValues {
            return PaddingValues(
                end = if (!LocalInspectionMode.current) {
                    with(receiver = LocalDensity.current) {
                        return@with getActionPadding.toDp()
                    }
                } else 0.dp,
            )
        }

        /**
         * 布局
         */
        @Composable
        override fun EbKitContent() {
            EbKitTheme {
                ActivityMain()
            }
        }

        /**
         * MainActivity布局
         */
        @Composable
        private fun ActivityMain() {
            var visible by remember { mutableStateOf(value = true) }
            val inspection: Boolean = LocalInspectionMode.current
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MPScreen(
                        popBackStack = {
                            visible = true
                        },
                    )
                    if (!inspection) AndroidView(
                        factory = { mViewFactory.getOverlayView },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = visible,
                ) {
                    NavigationRoot(
                        showMPLayer = {
                            visible = false
                        })
                }
            }
        }

        @Composable
        private fun NavigationRoot(modifier: Modifier = Modifier, showMPLayer: () -> Unit) {
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
            val adaptiveInfo = currentWindowAdaptiveInfo()
            val customNavSuiteType: NavigationSuiteType = with(adaptiveInfo) {
                return@with when (windowSizeClass.windowWidthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> NavigationSuiteType.NavigationBar
                    WindowWidthSizeClass.MEDIUM -> NavigationSuiteType.NavigationRail
                    WindowWidthSizeClass.EXPANDED -> NavigationSuiteType.NavigationDrawer
                    else -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                        adaptiveInfo
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
                            modifier = Modifier, enabled = true,
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
                            badge = {
                                Badge()
                            },
                        )
                    }
                },
                modifier = modifier,
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
                            showMPLayer = showMPLayer,
                        )
                    }
                    composable<MainActivity.Settings> {
                        SettingsDestination()
                    }
                }
            }
        }

        @Composable
        private fun HomeDestination(
            modifier: Modifier = Modifier,
            navController: NavController,
            showMPLayer: () -> Unit,
        ) {

            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                rememberTopAppBarState()
            )
            var expanded: Boolean by remember {
                mutableStateOf(false)
            }
            val scroll = rememberScrollState()
            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection
                    ),
                topBar = {
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        colors = TopAppBarDefaults.topAppBarColors(),
                        title = {
                            Text("Home")
                        },
                        actions = {
                            IconButton(
//                                modifier = Modifier.padding(
//                                    paddingValues = capsulePadding,
//                                ),
                                onClick = {
                                    expanded = !expanded
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
                                            Text("Settings")
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
                                            Text("About")
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
                                    DropdownMenuItem(
                                        text = {
                                            Text("小程序")
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                                contentDescription = null,
                                            )
                                        },
                                        onClick = {
                                            expanded = !expanded
                                            showMPLayer()
                                        },
                                    )
                                }
                            }
                        },
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


                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(100) {
                            Text(
                                text = "Item $it", modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }


                }
            }
        }

        @Composable
        private fun SettingsDestination() {
//            val capsulePadding: PaddingValues = rememberCapsulePadding()
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
                        actions = {
                            // .padding(paddingValues = capsulePadding)
                        },
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


        @Composable
        private fun MPOverlay(
            modifier: Modifier = Modifier,
            content: @Composable BoxScope.() -> Unit,
        ) {

        }

        @Composable
        private fun MPScreen(
            modifier: Modifier = Modifier,
            popBackStack: () -> Unit,
        ) {
            val scrollState = rememberScrollState()
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    MPTopBar()
                },
                bottomBar = {
                    MPBottomBar(popBackStack = popBackStack)
                },
                containerColor = Color(color = 0xff1B1B2B),
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                        .verticalScroll(state = scrollState),
                ) {
                    Box(
                        modifier = Modifier.padding(
                            start = 30.dp,
                            bottom = 15.dp,
                            top = 30.dp,
                        ),
                    ) {
                        Text(
                            text = "音乐和视频",
                            fontSize = 14.sp,
                            color = Color(
                                color = 0xFF8E8E9E,
                            ),
                        )
                    }
                    MPPlayer()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                start = 30.dp,
                                bottom = 15.dp,
                                top = 30.dp,
                                end = 30.dp,
                            ),
                    ) {
                        Row {
                            Box(
                                modifier = Modifier.weight(weight = 1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "最近使用小程序",
                                    fontSize = 14.sp,
                                    color = Color(color = 0xff8E8E9E),
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(weight = 1f)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "更多",
                                        fontSize = 14.sp,
                                        color = Color(color = 0xff8E8E9E),
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        modifier = Modifier.size(size = 16.dp),
                                        tint = Color(color = 0xff8E8E9E),
                                    )
                                }
                            }
                        }
                    }
                    AppsGrid(list = miniProgramList)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                start = 30.dp,
                                bottom = 15.dp,
                                top = 30.dp,
                            ),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = "我的小程序",
                            fontSize = 14.sp,
                            color = Color(color = 0xff8E8E9E),
                        )
                    }
                    AppsGrid(
                        modifier = Modifier.padding(bottom = 16.dp),
                        list = mineMiniProgramList,
                    )
                }
            }
        }

        @Composable
        private fun MPTopBar(modifier: Modifier = Modifier) {
            val capsulePadding: PaddingValues = rememberCapsulePadding()
            val textState = remember { mutableStateOf(value = TextFieldValue()) }
            Column(modifier = modifier.fillMaxWidth()) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "最近",
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                enabled = false,
                                value = textState.value,
                                onValueChange = { value ->
                                    textState.value = value
                                },
                                textStyle = TextStyle(
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier
                                    .width(width = 85.dp)
                                    .height(height = 25.dp)
                                    .padding(start = 20.dp),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(shape = RoundedCornerShape(size = 20.dp))
                                            .background(Color(color = 0xff434056))
                                            .padding(start = 6.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = null,
                                                modifier = Modifier.size(size = 20.dp),
                                                tint = Color(color = 0xff8E8E9E)
                                            )
                                            Box(
                                                modifier = Modifier.wrapContentSize(),
                                                contentAlignment = Alignment.CenterStart,
                                            ) {
                                                Text(
                                                    text = "搜索",
                                                    fontSize = 13.sp,
                                                    color = Color(color = 0xff8E8E9E),
                                                    textAlign = TextAlign.Center,
                                                )
                                                innerTextField()
                                            }
                                        }
                                    }
                                },
                            )
                        }
                    },
                    actions = {
                        Spacer(
                            modifier = Modifier.padding(
                                paddingValues = capsulePadding,
                            ),
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = Color(color = 0x22000000)
                )
            }
        }

        @Composable
        private fun MPBottomBar(
            modifier: Modifier = Modifier,
            popBackStack: () -> Unit,
        ) {
            BottomAppBar(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                actions = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "小程序",
                        maxLines = 1,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = {
                        Text("Back")
                    },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        },
                        onClick = popBackStack,
                        modifier = Modifier.wrapContentSize(),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                },
                containerColor = Color(color = 0xff787493),
            )
        }

        @Composable
        private fun AppsGrid(modifier: Modifier = Modifier, list: ArrayList<MiniProgramItem>) {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .height(height = 180.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                columns = GridCells.Fixed(count = 4),
                userScrollEnabled = false,
            ) {
                items(items = list) { item ->
                    Box {
                        AppItem(
                            style = AppItemStyle.Image,
                            appIcon = rememberImagePainter(data = item.icon),
                            appName = item.title,
                        )
                    }
                }
            }
        }

        @Composable
        private fun AppItem(
            modifier: Modifier = Modifier,
            onLaunch: () -> Unit = {},
            style: AppItemStyle,
            appIcon: Painter,
            appName: String,
        ) {
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(size = 60.dp)
                        .clip(shape = RoundedCornerShape(size = 35.dp))
                        .background(color = Color(color = 0xff434056))
                        .clickable(onClick = onLaunch),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = appIcon,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = when (style) {
                            AppItemStyle.Image -> Modifier
                                .fillMaxSize()
                                .clip(
                                    shape = RoundedCornerShape(
                                        size = 35.dp,
                                    ),
                                )

                            AppItemStyle.Icon -> Modifier.size(
                                size = 30.dp,
                            )
                        }
                    )
                }
                Text(
                    text = appName,
                    fontSize = 15.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        @Composable
        private fun MPPlayer(modifier: Modifier = Modifier) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(height = 90.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(weight = 1f),
                ) {
                    AppItem(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxSize(),
                        style = AppItemStyle.Icon,
                        appIcon = painterResource(id = R.drawable.icon_music),
                        appName = "音乐",
                    )
                    AppItem(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxSize(),
                        style = AppItemStyle.Icon,
                        appIcon = painterResource(id = R.drawable.icon_audio),
                        appName = "音频",
                    )
                }
                RecentPlayer(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxSize(),
                )
            }
        }

        @Composable
        private fun RecentPlayer(modifier: Modifier = Modifier) {
            val textState = remember { mutableStateOf(value = TextFieldValue()) }
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.height(height = 60.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 60.dp)
                            .padding(start = 10.dp),
                        enabled = false,
                        value = textState.value,
                        onValueChange = {
                            textState.value = it
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(40.dp))
                                    .background(Color(0xff434056))
                                    .padding(start = 6.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color(0xff8E8E9E)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(start = 10.dp),
                                        contentAlignment = Alignment.CenterStart,
                                    ) {
                                        Text(
                                            text = "暂无内容",
                                            fontSize = 16.sp,
                                            color = Color(0xff8E8E9E),
                                            textAlign = TextAlign.Center,
                                        )
                                        innerTextField()
                                    }
                                }
                            }
                        },
                    )
                }
                Text(
                    text = "最近播放",
                    fontSize = 15.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        /**
         * 自定义去掉水波纹的点击拓展函数
         */
        private fun Modifier.unwaveClick(
            onClick: () -> Unit,
        ): Modifier = composed {
            clickable(
                indication = null,
                onClick = onClick,
                interactionSource = remember {
                    MutableInteractionSource()
                },
            )
        }
    }

    /**
     ***********************************************************************************************
     *
     * 私有函数
     *
     ***********************************************************************************************
     */

    private fun generatorSdkPointByPosition(): List<Point> {
        sdkBannerPointList.clear()
        sdkBannerPointList.add(
            Point(
                x = 0F,
                y = bannerDistanceOriginPointLength - bannerWidth.toFloat(),
            ),
        )
        sdkBannerPointList.add(
            Point(
                x = bannerDistanceOriginPointLength - bannerWidth.toFloat(),
                y = 0F,
            ),
        )
        sdkBannerPointList.add(
            Point(
                x = bannerDistanceOriginPointLength.toFloat(),
                y = 0F,
            ),
        )
        sdkBannerPointList.add(
            Point(
                x = 0F,
                y = bannerDistanceOriginPointLength.toFloat(),
            ),
        )
        return sdkBannerPointList
    }


    /**
     * 绘制横幅
     */
    private fun drawSdkBanner(canvas: Canvas, pointList: List<Point>) {
        sdkBannerPath.apply {
            reset()
            pointList.withIndex().forEach {
                if (it.index == 0) {
                    moveTo(it.value.x, it.value.y)
                } else {
                    lineTo(it.value.x, it.value.y)
                }
            }
        }
        canvas.drawPath(sdkBannerPath, sdkBannerPaint)
    }

    /**
     * 绘制横幅上的文字
     */
    private fun ViewGroup.drawSdkText(canvas: Canvas, pointList: List<Point>) {
        // 测量欲绘制文字宽度
        val bannerTextWidth = sdkBannerTextPaint.measureText(sdkBannerText)
        // 计算banner最短边长度
        val bannerShortestLength = (sqrt(
            2 * (bannerDistanceOriginPointLength - bannerWidth).toDouble().pow(2)
        )).toFloat()
        if (bannerTextWidth > bannerShortestLength) {
            // 如果最短边长度小于欲绘制文字长度,则对欲绘制文字剪裁,直到欲绘制文字比最短边长度小方可绘制文字
            sdkBannerText = sdkBannerText.substring(0, sdkBannerText.length - 1)
            drawSdkText(canvas, pointList)
            return
        }
        // 计算banner最长边长度
        val bannerLongestLength =
            (sqrt(2 * (bannerDistanceOriginPointLength).toDouble().pow(2))).toFloat()
        val hOffset = bannerShortestLength / 2 - bannerTextWidth / 2
        // 单个直角边长度
        val oneOfTheRightAngleLength = (bannerLongestLength - bannerShortestLength) / 2
        // 计算banner的高度
        val bannerHeight =
            sqrt(bannerWidth.toDouble().pow(2) - oneOfTheRightAngleLength.pow(2)).toFloat()
        val fontMetrics = sdkBannerTextPaint.fontMetrics
        // 计算baseLine偏移量
        val baseLineOffset = (fontMetrics.top + fontMetrics.bottom) / 2
        val vOffset = bannerHeight / 2 - baseLineOffset
        canvas.drawTextOnPath(
            sdkBannerText,
            sdkBannerPath,
            hOffset,
            vOffset,
            sdkBannerTextPaint,
        )
    }


    private fun generatorDebugPointByPosition(): List<Point> {
        debugBannerPointList.clear()
        debugBannerPointList.add(
            element = Point(
                x = viewWidth - (bannerDistanceOriginPointLength - bannerWidth).toFloat(), y = 0F
            ),
        )
        debugBannerPointList.add(
            element = Point(
                x = viewWidth.toFloat(), y = bannerDistanceOriginPointLength - bannerWidth.toFloat()
            ),
        )
        debugBannerPointList.add(
            element = Point(
                x = viewWidth.toFloat(),
                y = bannerDistanceOriginPointLength.toFloat(),
            ),
        )
        debugBannerPointList.add(
            element = Point(
                x = viewWidth.toFloat() - bannerDistanceOriginPointLength, y = 0F
            ),
        )
        return debugBannerPointList
    }

    private fun drawDebugBanner(canvas: Canvas, pointList: List<Point>) {
        debugBannerPath.apply {
            reset()
            pointList.withIndex().forEach {
                if (it.index == 0) {
                    moveTo(it.value.x, it.value.y)
                } else {
                    lineTo(it.value.x, it.value.y)
                }
            }
        }
        canvas.drawPath(debugBannerPath, debugBannerPaint)
    }

    private fun ViewGroup.drawDebugText(canvas: Canvas, pointList: List<Point>) {
        // 测量欲绘制文字宽度
        val bannerTextWidth = debugBannerTextPaint.measureText(debugBannerText)
        // 计算banner最短边长度
        val bannerShortestLength = (sqrt(
            2 * (bannerDistanceOriginPointLength - bannerWidth).toDouble().pow(2)
        )).toFloat()
        if (bannerTextWidth > bannerShortestLength) {
            // 如果最短边长度小于欲绘制文字长度,则对欲绘制文字剪裁,直到欲绘制文字比最短边长度小方可绘制文字
            debugBannerText = debugBannerText.substring(0, debugBannerText.length - 1)
            drawDebugText(canvas, pointList)
            return
        }
        // 计算banner最长边长度
        val bannerLongestLength =
            (sqrt(2 * (bannerDistanceOriginPointLength).toDouble().pow(2))).toFloat()
        val hOffset = bannerShortestLength / 2 - bannerTextWidth / 2
        // 单个直角边长度
        val oneOfTheRightAngleLength = (bannerLongestLength - bannerShortestLength) / 2
        // 计算banner的高度
        val bannerHeight =
            sqrt(bannerWidth.toDouble().pow(2) - oneOfTheRightAngleLength.pow(2)).toFloat()
        val fontMetrics = debugBannerTextPaint.fontMetrics
        // 计算baseLine偏移量
        val baseLineOffset = (fontMetrics.top + fontMetrics.bottom) / 2
        val vOffset = bannerHeight / 2 - baseLineOffset
        canvas.drawTextOnPath(
            debugBannerText,
            debugBannerPath,
            hOffset,
            vOffset,
            debugBannerTextPaint,
        )
    }

    private fun ViewGroup.drawCapsuleFill(canvas: Canvas) {
        setRoundRect(capsuleFillPath)
        canvas.drawPath(capsuleFillPath, capsuleFillPaint)
    }

    private fun ViewGroup.drawCapsuleStroke(canvas: Canvas) {
        setRoundRect(capsuleStrokePath)
        canvas.drawPath(capsuleStrokePath, capsuleStrokePaint)
    }

    private fun ViewGroup.drawCapsuleDivider(canvas: Canvas) {
        canvas.drawLine(
            (viewWidth - (capsuleWidth / 2) - capsuleRightPadding - paddingRight).toFloat(),
            (capsuleTopPadding + paddingTop + 4.toDp).toFloat(),
            (viewWidth - (capsuleWidth / 2) - capsuleRightPadding - paddingRight).toFloat(),
            (capsuleTopPadding + capsuleHeight + paddingTop - 4.toDp).toFloat(),
            capsuleDividerPaint,
        )
    }

    private fun ViewGroup.setRoundRect(path: Path) {
        path.reset()
        path.addRoundRect(
            RectF(
                (viewWidth - capsuleWidth - capsuleRightPadding - paddingRight).toFloat(),
                (capsuleTopPadding + paddingTop).toFloat(),
                (viewWidth - capsuleRightPadding - paddingRight).toFloat(),
                (capsuleTopPadding + capsuleHeight + paddingTop).toFloat()
            ),
            capsuleRadius,
            capsuleRadius,
            Path.Direction.CW,
        )
    }

    private val IContent.getActionPadding: Int
        get() = capsuleWidth + capsuleRightPadding + mComposeOverlay.paddingRight


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
        setSupportActionBar(mToolbar)
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

    /**
     * 转换为DP
     */
    private val Int.toDp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this@toDp.toFloat(),
            Resources.getSystem().displayMetrics,
        ).toInt()

    /**
     * 转换为SP
     */
    private val Int.toSp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this@toSp.toFloat(),
            Resources.getSystem().displayMetrics,
        ).toInt()

    private val Float.toDp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics,
        ).toInt()

    private val Float.toSp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics,
        ).toInt()

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
        /** 服务动作 */
        const val ACTION: String = "io.freefeos.sdk.action"

        const val WECHAT_PACKAGE: String = "com.tencent.mm"

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
    private companion object {

        /** 用于打印日志的标签 */
        const val TAG: String = "MainActivity"

        /** 操作栏是否应该在[AUTO_HIDE_DELAY_MILLIS]毫秒后自动隐藏。*/
        const val AUTO_HIDE: Boolean = false

        /** 如果设置了[AUTO_HIDE]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val AUTO_HIDE_DELAY_MILLIS: Int = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val UI_ANIMATOR_DELAY: Int = 300
    }
}


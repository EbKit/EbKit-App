package io.ebkit.app.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.ebkit.app.BuildConfig
import io.ebkit.app.R
import io.ebkit.app.ui.theme.capsuleHeight
import io.ebkit.app.ui.theme.capsuleRadius
import io.ebkit.app.ui.theme.capsuleRightPadding
import io.ebkit.app.ui.theme.capsuleWidth
import io.ebkit.app.utils.toDp
import io.ebkit.app.utils.toSp
import kotlin.math.pow
import kotlin.math.sqrt

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var viewWidth = 0
    private var viewHeight = 0

    /** 胶囊顶部边距 */
    private val capsuleTopPadding get() = ((actionBarHeight.toDp) - capsuleHeight.toDp) / 2


    private val actionBarHeight: Int = 65

    /** 是否显示调试信息 */
    private val show: Boolean = BuildConfig.DEBUG

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

    private data class Point(
        val x: Float,
        val y: Float,
    )


    /**
     * 初始化
     */
    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val currentSafeInsets = insets.getInsets(
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
        addView(
            AppCompatImageButton(context).apply {
                tag = MENU_BUTTON_TAG
            },
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
            ),
        ) // 添加菜单按钮
        addView(
            AppCompatImageButton(context).apply {
                tag = CLOSE_BUTTON_TAG
            },
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
            ),
        ) // 添加关闭按钮
        setWillNotDraw(false) // 启用内容绘制
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.let { childView ->
            when (childView.tag) {
                MENU_BUTTON_TAG -> (childView as AppCompatImageButton).apply {
                    setBackgroundColor(Color.Transparent.toArgb())
                    setImageResource(R.drawable.baseline_more_horiz_24)
                    setOnClickListener {
                        Toast.makeText(
                            child.context,
                            "mMenuButton",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                CLOSE_BUTTON_TAG -> (childView as AppCompatImageButton).apply {
                    setBackgroundColor(Color.Transparent.toArgb())
                    setImageResource(R.drawable.baseline_close_24)
                    setOnClickListener {
                        Toast.makeText(
                            child.context,
                            "mCloseButton",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                else -> error(
                    message = "unknown instance"
                )
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
        // 起始位置偏移量，用于放置第一个按钮
        var leftOffset = 0
        // 遍历子视图
        for (index in 0 until childCount) {
            when (index) {
                // 仅布局前两个子视图
                0, 1 -> {
                    // 布局子视图
                    getChildAt(index).layout(
                        // 按钮的左侧坐标
                        // 视图宽度 - 视图右边距 - 胶囊按钮右边距 - 胶囊按钮宽度 + 左侧偏移量
                        viewWidth - paddingRight - capsuleRightPadding.toDp - capsuleWidth.toDp + leftOffset,
                        // 按钮的顶部坐标
                        // 胶囊按钮顶部边距 + 视图顶部边距
                        capsuleTopPadding + paddingTop,
                        // 按钮的右侧坐标
                        // 视图宽度 - 视图右边距 - 胶囊按钮右边距 - 胶囊按钮宽度 + 左侧偏移量 + 胶囊按钮宽度的一半
                        viewWidth - paddingRight - capsuleRightPadding.toDp - capsuleWidth.toDp + leftOffset + capsuleWidth.toDp / 2,
                        // 按钮的底部坐标
                        // 胶囊按钮顶部边距 + 胶囊按钮高度 + 视图顶部边距
                        capsuleTopPadding + capsuleHeight.toDp + paddingTop,
                    )
                    // 更新下一个按钮的起始位置
                    leftOffset += capsuleWidth.toDp / 2
                }
                // 跳出循环,拒绝其他子视图布局
                else -> continue
            }
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
            (viewWidth - (capsuleWidth.toDp / 2) - capsuleRightPadding.toDp - paddingRight).toFloat(),
            (capsuleTopPadding + paddingTop + 4.toDp).toFloat(),
            (viewWidth - (capsuleWidth.toDp / 2) - capsuleRightPadding.toDp - paddingRight).toFloat(),
            (capsuleTopPadding + capsuleHeight.toDp + paddingTop - 4.toDp).toFloat(),
            capsuleDividerPaint,
        )
    }

    private fun ViewGroup.setRoundRect(path: Path) {
        path.reset()
        path.addRoundRect(
            RectF(
                (viewWidth - capsuleWidth.toDp - capsuleRightPadding.toDp - paddingRight).toFloat(),
                (capsuleTopPadding + paddingTop).toFloat(),
                (viewWidth - capsuleRightPadding.toDp - paddingRight).toFloat(),
                (capsuleTopPadding + capsuleHeight.toDp + paddingTop).toFloat()
            ),
            capsuleRadius.toDp.toFloat(),
            capsuleRadius.toDp.toFloat(),
            Path.Direction.CW,
        )
    }

    companion object {
        private const val MENU_BUTTON_TAG: String = "menu"
        private const val CLOSE_BUTTON_TAG: String = "close"
    }
}
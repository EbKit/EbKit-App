package io.ebkit.app.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AbstractComposeView

class HybridComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr,
) {

    /**
     * 初始化
     */
    init {
        // 必须加此代码否则不符合预期
        // 支持混合开发
        consumeWindowInsets = false
    }

    private val content = mutableStateOf<(@Composable () -> Unit)?>(value = null)

    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    @Composable
    override fun Content() {
        content.value?.invoke()
    }

    /**
     * 无障碍
     */
    override fun getAccessibilityClassName(): CharSequence {
        return javaClass.name
    }

    internal fun setContent(
        content: @Composable () -> Unit,
    ) {
        shouldCreateCompositionOnAttachedToWindow = true
        this@HybridComposeView.content.value = content
        if (isAttachedToWindow) {
            createComposition()
        }
    }
}
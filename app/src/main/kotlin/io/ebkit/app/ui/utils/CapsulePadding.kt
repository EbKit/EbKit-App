package io.ebkit.app.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ebkit.app.IViewFactory
import io.ebkit.app.ui.theme.capsuleRightPadding
import io.ebkit.app.ui.theme.capsuleWidth

/**
 * 获取胶囊按钮右填充
 */
@Composable
@ReadOnlyComposable
fun rememberCapsulePadding(
    factory: IViewFactory? = null,
    excess: Dp = 0.dp,
): PaddingValues {
    val density: Density = LocalDensity.current
    val inspection = LocalInspectionMode.current
    val basePadding = capsuleWidth + capsuleRightPadding
    return PaddingValues(
        end = when {
            inspection -> basePadding

            else -> with(receiver = density) {
                val system = (factory?.getOverlayView?.paddingRight ?: 0).toDp()
                return@with (basePadding + system) - excess
            }
        },
    )
}
package io.ebkit.app.ui.utils

import android.app.Activity
import android.view.View
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ebkit.app.IViewFactory
import io.ebkit.app.MainActivity
import io.ebkit.app.ui.theme.capsuleRightPadding
import io.ebkit.app.ui.theme.capsuleWidth
import io.ebkit.app.utils.toDp

/**
 * 获取胶囊按钮右填充
 */
@Composable
fun rememberCapsulePadding(
    factory: IViewFactory? = null,
    excess: Dp = 0.dp,
): PaddingValues {
    val activity: Activity? = LocalActivity.current
    val density: Density = LocalDensity.current
    val inspection = LocalInspectionMode.current
    val overlay: View? = factory?.getOverlayView
    return PaddingValues(
        end = when  {
            inspection -> 0.dp

            activity?.javaClass == MainActivity::class.java -> with(receiver = density) {
                capsuleWidth.toDp.toDp() + capsuleRightPadding.toDp.toDp() + (overlay?.paddingRight?:0).toDp() - excess
            }

            else -> 0.dp
        },
    )
}
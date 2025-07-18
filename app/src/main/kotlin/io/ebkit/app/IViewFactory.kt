package io.ebkit.app

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.compose.ui.platform.AbstractComposeView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar

interface IViewFactory {
    val getContentFrame: FrameLayout
    val getContentView: AbstractComposeView
    val getOverlayView: FrameLayout
    val getToolbarView: MaterialToolbar
    val getFlutterView: ViewPager2
    val getMenuButton: AppCompatImageButton
    val getCloseButton: AppCompatImageButton

    val fillMaxSize: ViewGroup.LayoutParams
    val wrapContentSize: ViewGroup.LayoutParams
}

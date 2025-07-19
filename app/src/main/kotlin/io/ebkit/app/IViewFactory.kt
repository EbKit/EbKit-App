package io.ebkit.app

import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import io.ebkit.app.ui.view.HybridComposeView
import io.ebkit.app.ui.view.OverlayView

interface IViewFactory {
    val getContentFrame: FrameLayout
    val getContentView: HybridComposeView
    val getOverlayView: OverlayView
    val getToolbarView: MaterialToolbar
    val getFlutterView: ViewPager2
}

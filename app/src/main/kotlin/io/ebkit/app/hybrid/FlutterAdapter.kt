package io.ebkit.app.hybrid

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flutter.embedding.android.FlutterFragment

class FlutterAdapter(
    activity: FragmentActivity,
    flutter: FlutterFragment?,
) : FragmentStateAdapter(
    activity,
) {

    /** Flutter */
    private val mFlutter = flutter

    /** 获取 Fragment 数量 */
    override fun getItemCount() = 1

    /** 创建 Fragment */
    override fun createFragment(position: Int): Fragment {
        return mFlutter ?: error(
            message = "Flutter is null!",
        )
    }
}
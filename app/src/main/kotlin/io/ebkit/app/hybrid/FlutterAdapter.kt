package io.ebkit.app.hybrid

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flutter.embedding.android.FlutterFragment

class FlutterAdapter(
    activity: FragmentActivity,
    flutter: FlutterFragment?,
) : FragmentStateAdapter(activity) {
    private val mFlutterFragment = flutter

    override fun getItemCount() = 1

    override fun createFragment(position: Int): Fragment {
        return mFlutterFragment ?: error(
            message = "mFlutterFragment is null!",
        )
    }
}
package io.ebkit.app.ui.utils

import android.content.Context
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/** 将 [Dp] 转换为 [Int] 像素值. */
fun convertDpToPx(context: Context, value: Dp): Int {
    return with(receiver = Density(context = context)) {
        return@with value.toPx().toInt()
    }
}

/** 将 [TextUnit] 转换为 [Int] 像素值. */
fun convertSpToPx(context: Context, value: TextUnit): Int {
    return with(receiver = Density(context = context)) {
        return@with value.toPx().toInt()
    }
}
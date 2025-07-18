package io.ebkit.app.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * 转换为DP
 */
val Int.toDp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this@toDp.toFloat(),
        Resources.getSystem().displayMetrics,
    ).toInt()

/**
 * 转换为SP
 */
val Int.toSp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this@toSp.toFloat(),
        Resources.getSystem().displayMetrics,
    ).toInt()

val Float.toDp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics,
    ).toInt()

val Float.toSp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics,
    ).toInt()
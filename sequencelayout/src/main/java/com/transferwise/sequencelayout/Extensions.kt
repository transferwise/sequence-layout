package com.transferwise.sequencelayout

import android.content.res.Resources
import android.view.ViewGroup

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun ViewGroup.children() = 0.until(childCount).map { getChildAt(it) }
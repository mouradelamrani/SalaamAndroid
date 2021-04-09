package com.mouradelamrani.salaam.extensions

import android.content.ComponentName
import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import com.mouradelamrani.salaam.utils.AndroidScreen
import kotlin.math.roundToInt

fun Context.getProviderAuthority(providerName: String): String {
    val componentName = ComponentName(this, providerName)
    val providerInfo = this.packageManager.getProviderInfo(componentName, 0)
    return providerInfo.authority
}

fun Context.dpToPxFloat(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
fun Context.dpToPxFloat(dp: Int): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).roundToInt()
fun Context.dpToPx(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).roundToInt()
fun Context.screenSize(): Point = AndroidScreen.screenSizePx(this)
fun Context.screenSizeDp(): Point = AndroidScreen.screenSizeDp(this)
fun Context.screenHeight(): Int = AndroidScreen.screenSizePx(this).y
fun Context.screenWidth(): Int = AndroidScreen.screenSizePx(this).x
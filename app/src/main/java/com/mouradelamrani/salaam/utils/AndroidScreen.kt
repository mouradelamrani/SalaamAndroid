package com.mouradelamrani.salaam.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import java.lang.reflect.Field
import kotlin.math.roundToInt


/**
 * Provides utility methods for working with the device screen.
 */
object AndroidScreen {

    /**
     * Converts the given device independent pixels (DP) value into the corresponding pixels
     * value for the current screen.
     *
     * @param context Context instance
     * @param dp     The DP value to convert
     * @return The pixels value for the current screen of the given DIP value.
     */
    fun dpToPx(context: Context, dp: Int): Float {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)
    }

    /**
     * Converts the given device independent pixels (DP) value into the corresponding pixels
     * value for the current screen.
     *
     * @param context Context instance
     * @param dp     The DP value to convert
     * @return The pixels value for the current screen of the given DIP value.
     */
    fun dpToPx(context: Context, dp: Float): Float {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
    }

    /**
     * Converts the given pixels value into the corresponding device independent pixels (DP)
     * value for the current screen.
     *
     * @param context Context instance
     * @param pixels  The pixels value to convert
     * @return The DP value for the current screen of the given pixels value.
     */
    fun pxToDp(context: Context, pixels: Int): Float {
        val displayMetrics = context.resources.displayMetrics
        return pixels / (displayMetrics.densityDpi / 160f)
    }

    /**
     * Returns the current screen dimensions in device independent pixels (DP) as a [Point] object where
     * [Point.x] is the screen width and [Point.y] is the screen height.
     *
     * @param context Context instance
     * @return The current screen dimensions in DP.
     */
    fun screenSizeDp(context: Context): Point {
        val configuration = context.resources.configuration
        return Point(configuration.screenWidthDp, configuration.screenHeightDp)
    }

    fun screenSizePx(context: Context): Point {
        val configuration = context.resources.configuration
        return Point(
            dpToPx(context, configuration.screenWidthDp).roundToInt(),
            dpToPx(context, configuration.screenHeightDp).roundToInt()
        )
    }


    /**
     * @param context Context instance
     * @return [true] if the device is in landscape orientation, [false] otherwise.
     */
    fun isLandscape(context: Context): Boolean {
        val configuration = context.resources.configuration
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    // TODO: must be tested on various of devices
    /**
     * Returns bottom navigation bar height in pixels and 0 if there is no navigation bar or it's
     * not in the bottom.
     */
    fun getBottomNavigationBarHeight(context: Context): Int {
        val appUsableSize = getAppUsableScreenSize(context)
        val realScreenSize = getRealScreenSize(context)

        return realScreenSize.y - appUsableSize.y
    }

    /**
     * Returns true if there is navigation bar on the screen.
     */
    fun hasNavigationBar(context: Context): Boolean = getBottomNavigationBarHeight(context) != 0

    fun getNavigationBarDividerHeight(context: Context): Float =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) dpToPx(context, 1.25f)
        else 0f


    // region private

    private fun getAppUsableScreenSize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    private fun getRealScreenSize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size
    }

    // endregion


    private var attachInfoField: Field? = null
    private var stableInsetsField: Field? = null

    @TargetApi(VERSION_CODES.LOLLIPOP)
    fun getViewInset(view: View): Int {
        try {
            if (attachInfoField == null) {
                attachInfoField = View::class.java.getDeclaredField("mAttachInfo")
                attachInfoField!!.isAccessible = true
            }
            val attachInfo = attachInfoField!!.get(view)
            if (attachInfo != null) {
                if (stableInsetsField == null) {
                    stableInsetsField = attachInfo.javaClass.getDeclaredField("mStableInsets")
                    stableInsetsField!!.isAccessible = true
                }
                val insets = stableInsetsField!!.get(attachInfo) as Rect
                return insets.bottom
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return 0
    }

    fun isCurrentModeNight(context: Context): Boolean =
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

    fun keyboardHeight(context: Context): Int = screenSizePx(context).y * 3 / 10

}
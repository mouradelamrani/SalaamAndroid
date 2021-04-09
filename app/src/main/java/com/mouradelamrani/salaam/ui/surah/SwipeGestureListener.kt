package com.mouradelamrani.salaam.ui.surah

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class SwipeGestureListener(
    private val onLeft: (() -> Unit)? = null,
    private val onUp: (() -> Unit)? = null,
    private val onRight: (() -> Unit)? = null,
    private val onDown: (() -> Unit)? = null
) : GestureDetector.SimpleOnGestureListener() {

    companion object {
        private val SWIPE_MIN_DISTANCE = 120
        private val SWIPE_MAX_OFF_PATH = 250
        private val SWIPE_THRESHOLD_VELOCITY = 200
    }

    override fun onDown(e: MotionEvent): Boolean {
        log("onDown: $e")
        return true
    }

    override fun onFling(
        event1: MotionEvent, event2: MotionEvent,
        velocityX: Float, velocityY: Float
    ): Boolean {
        log("onFling: $event1 $event2")
        return when {
            abs(event1.y - event2.y) > SWIPE_MAX_OFF_PATH && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY -> {
                log("swipe down")
                onDown?.invoke()
                onDown != null
            }
            abs(event2.y - event1.y) > SWIPE_MAX_OFF_PATH && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY -> {
                log("swipe up")
                onUp?.invoke()
                onUp != null
            }
            event1.x - event2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY -> {
                log("left swipe")
                onLeft?.invoke()
                onLeft != null
            }
            event2.x - event1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY -> {
                log("right swipe")
                onRight?.invoke()
                onRight != null
            }
            else -> false

        }
    }

    private fun log(msg: String) = Log.d(this::class.java.simpleName, msg)
}
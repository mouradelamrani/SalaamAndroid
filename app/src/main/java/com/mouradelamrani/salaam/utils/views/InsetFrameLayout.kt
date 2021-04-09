package com.mouradelamrani.salaam.utils.views

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout


/**
 * FrameLayout that dispatch window insets to all children rather than only to first one.
 */
class InsetFrameLayout : FrameLayout {


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onApplyWindowInsets(windowInsets: WindowInsets): WindowInsets {
        for (i in 0 until childCount) getChildAt(i).dispatchApplyWindowInsets(windowInsets)
        return windowInsets
    }
}
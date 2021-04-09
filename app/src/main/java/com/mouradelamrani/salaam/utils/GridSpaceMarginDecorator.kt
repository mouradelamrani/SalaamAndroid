package com.mouradelamrani.salaam.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


class GridSpaceMarginDecorator(
    @Px val verticalSpace: Float,
    @Px val horizontalSpace: Float
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount
        if (position == RecyclerView.NO_POSITION || itemCount == null) return

        val top = if (position >= spanCount) verticalSpace else 0f
        val left = if (position % spanCount != 0) horizontalSpace / 2f else 0f
        val right = if (position % spanCount != spanCount - 1) horizontalSpace / 2f else 0f

        outRect.set(left.roundToInt(), top.roundToInt(), right.roundToInt(), 0)
    }

}
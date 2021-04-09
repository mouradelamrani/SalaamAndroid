package com.mouradelamrani.salaam.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FirstLastLinearMarginDecorator(var firstMargin: Int, var lastMargin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager !is LinearLayoutManager || parent.adapter == null) return

        when (parent.layoutManager) {
            is GridLayoutManager -> {
                val layoutManager = parent.layoutManager as GridLayoutManager
                val position = parent.getChildAdapterPosition(view)
                if (position < layoutManager.spanCount) {
                    if (layoutManager.orientation == GridLayoutManager.VERTICAL)
                        if (layoutManager.reverseLayout) outRect.bottom = this.firstMargin else outRect.top = this.firstMargin
                    else if (layoutManager.reverseLayout) outRect.right = this.firstMargin else outRect.left = this.firstMargin
                }
                val itemCount = parent.adapter!!.itemCount
                var lastRowCount = itemCount % layoutManager.spanCount
                if (lastRowCount == 0) lastRowCount = layoutManager.spanCount
                if (position >= itemCount - lastRowCount) {
                    if (layoutManager.orientation == GridLayoutManager.VERTICAL)
                        if (layoutManager.reverseLayout) outRect.top = this.lastMargin else outRect.bottom = this.lastMargin
                    else if (layoutManager.reverseLayout) outRect.left = this.lastMargin else outRect.right = this.lastMargin
                }
            }
            is LinearLayoutManager -> {
                val layoutManager = parent.layoutManager as LinearLayoutManager
                val position = parent.getChildAdapterPosition(view)
                if (position == 0) {
                    if (layoutManager.orientation == LinearLayoutManager.VERTICAL)
                        if (layoutManager.reverseLayout) outRect.bottom = this.firstMargin else outRect.top = this.firstMargin
                    else if (layoutManager.reverseLayout) outRect.right = this.firstMargin else outRect.left = this.firstMargin
                }
                if (position == parent.adapter!!.itemCount - 1) {
                    if (layoutManager.orientation == LinearLayoutManager.VERTICAL)
                        if (layoutManager.reverseLayout) outRect.top = this.lastMargin else outRect.bottom = this.lastMargin
                    else if (layoutManager.reverseLayout) outRect.left = this.lastMargin else outRect.right = this.lastMargin
                }
            }
        }
    }
}

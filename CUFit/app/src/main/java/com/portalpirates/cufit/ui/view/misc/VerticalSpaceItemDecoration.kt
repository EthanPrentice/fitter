package com.portalpirates.cufit.ui.view.misc

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int, private val endingSpace: Boolean = false) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (endingSpace || parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

}
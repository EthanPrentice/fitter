package com.portalpirates.cufit.ui.util

import androidx.recyclerview.widget.RecyclerView

interface DragEventListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
    fun onEndDrag(viewHolder: RecyclerView.ViewHolder?)
}
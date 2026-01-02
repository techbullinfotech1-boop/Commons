package com.dh.app.core.interfaces

import androidx.recyclerview.widget.RecyclerView

interface StartReorderDragListener {
    fun requestDrag(viewHolder: RecyclerView.ViewHolder)
}

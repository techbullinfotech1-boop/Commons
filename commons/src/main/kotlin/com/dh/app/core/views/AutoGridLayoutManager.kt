package com.dh.app.core.views

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max


open class AutoGridLayoutManager(
    context: Context,
    private var itemWidth: Int
) : MyGridLayoutManager(context, 1) {

    init {
        require(itemWidth >= 0) {
            "itemWidth must be >= 0"
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        val width = width
        val height = height
        if (itemWidth > 0 && width > 0 && height > 0) {
            val totalSpace = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            spanCount = max(1, totalSpace / itemWidth)
        }
        super.onLayoutChildren(recycler, state)
    }
}

package com.jem.image2searchapp.util

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View

class RecyclerviewItemDeco(context : Context) : RecyclerView.ItemDecoration() {

    private var splitSpace : Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.right = splitSpace
    }

    // 간격 5
    init{
        this.splitSpace = fromDpToPx(context, 2)
    }

    fun fromDpToPx(context : Context, dp : Int): Int {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}
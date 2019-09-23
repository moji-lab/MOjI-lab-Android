package com.mojilab.moji.util.adapter

import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View

class RecyclerviewTagItemDeco(context : Context) : RecyclerView.ItemDecoration() {

    private var splitSpace : Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.right = splitSpace
    }

    // 간격 2
    init{
        this.splitSpace = fromDpToPx(context, 10)
    }

    fun fromDpToPx(context : Context, dp : Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}
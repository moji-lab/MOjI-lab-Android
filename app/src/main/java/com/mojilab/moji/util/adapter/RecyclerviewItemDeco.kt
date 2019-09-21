package com.mojilab.moji.util.adapter

import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View

class RecyclerviewItemDeco(context : Context, myPageTabPosition : Int) : RecyclerView.ItemDecoration() {

    private var splitSpace : Int = 0
    var tabPosition : Int = myPageTabPosition

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        var position : Int = parent.getChildAdapterPosition(view)

        // 스크랩 화면은 아래쪽도 간격 있음
        if(tabPosition == 2){
            outRect.bottom = splitSpace
        }
        // 마지막 아이템은 여백 X
        if(position != parent.adapter!!.itemCount - 1){
            outRect.right = splitSpace
        }
    }

    // 간격 2
    init{
        this.splitSpace = fromDpToPx(context, 2)
    }

    fun fromDpToPx(context : Context, dp : Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}
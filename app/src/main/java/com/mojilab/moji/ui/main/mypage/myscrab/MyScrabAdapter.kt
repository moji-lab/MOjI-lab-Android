package com.mojilab.moji.ui.main.mypage.myscrab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.data.FeedData

class MyScrabAdapter(var context : Context, private var recordDatas: ArrayList<FeedData>, var requestManager : RequestManager) : RecyclerView.Adapter<MyScrabViewHolder>(){

    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyScrabViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scrab_image, parent, false)
        mContext = context

        return MyScrabViewHolder(mainView)
    }

    override fun getItemCount(): Int = recordDatas.size

    override fun onBindViewHolder(holder: MyScrabViewHolder, position: Int) {

//        requestManager.load(recordDatas[position].!!.get(0)).into(holder.primaryImage)

    }
}
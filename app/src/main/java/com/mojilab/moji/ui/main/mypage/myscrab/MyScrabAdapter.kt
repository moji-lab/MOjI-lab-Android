package com.mojilab.moji.ui.main.mypage.myscrab

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity
import com.mojilab.moji.ui.main.mypage.data.FeedData

class MyScrabAdapter(var context : Context, private var scrapDatas: ArrayList<FeedData>, var requestManager : RequestManager) : RecyclerView.Adapter<MyScrabViewHolder>(){

    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyScrabViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scrab_image, parent, false)
        mContext = context

        return MyScrabViewHolder(mainView)
    }

    override fun getItemCount(): Int = scrapDatas.size

    override fun onBindViewHolder(holder: MyScrabViewHolder, position: Int) {

        Log.v(TAG, "스크랩 대표 이미지 = " + scrapDatas[position].photoList[0].photoUrl)
        requestManager.load(scrapDatas[position].photoList[0].photoUrl).into(holder.primaryImage)

        holder.primaryImage.setOnClickListener {
            var intent = Intent(mContext, DetailFeedActivity::class.java)
            intent.putExtra("boardIdx", scrapDatas[position].boardIdx)
            mContext.startActivity(intent)
        }
    }
}
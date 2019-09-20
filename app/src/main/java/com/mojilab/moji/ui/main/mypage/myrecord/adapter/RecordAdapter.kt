package com.mojilab.moji.ui.main.mypage.myrecord.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.myrecord.data.RecordData

class RecordAdapter(var context : Context, private var recordDatas: ArrayList<RecordData>, var requestManager : RequestManager) : RecyclerView.Adapter<RecordViewHolder>(){

    lateinit var recordImageAdapter: RecordImageAdapter
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)

        mContext = context

        return RecordViewHolder(mainView)
    }

    override fun getItemCount(): Int = recordDatas.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        Log.v("asdf", "포지션 = " + position)
        recordImageAdapter = RecordImageAdapter(recordDatas[position].recordImg!!, requestManager)

        requestManager.load(recordDatas[position].profileImgUrl).into(holder.profileImage)
        holder.profileName.text = recordDatas[position].name
        holder.recordDate.text = recordDatas[position].date

        holder.coarse.text = recordDatas[position].coarse
        holder.coarseContent.text = recordDatas[position].carseContent

        holder.recordImagesRv.adapter = recordImageAdapter
        holder.recordImagesRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        holder.likeNum.text = recordDatas[position].likeNum.toString()
        holder.commentNum.text = recordDatas[position].commentNum.toString()
    }
}
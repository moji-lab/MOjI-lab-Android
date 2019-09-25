package com.mojilab.moji.ui.main.mypage.notice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.data.NoticeData

class NoticeAdapter(private var noticeDatas: ArrayList<NoticeData>, var requestManager: RequestManager) : RecyclerView.Adapter<NoticeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(mainView)
    }

    override fun getItemCount(): Int = noticeDatas.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {

        /*
        // 프로필 사진 없을 때
        if(noticeDatas[position].profileImgUrl == null){
            holder.noticeProfileImg.visibility = View.GONE
            holder.noticeDefaultRl.visibility = View.VISIBLE
            holder.noticeProfileName.text = noticeDatas[position].name
        }
        // 프로필 사진 있을 때
        else{
            holder.noticeProfileImg.visibility = View.VISIBLE
            holder.noticeDefaultRl.visibility = View.GONE
            requestManager.load(noticeDatas[position].profileImgUrl).into(holder.noticeProfileImg)
        }

        */

        holder.noticeContentProfileName.text = noticeDatas[position].message.substring(0, 2)
        holder.noticeContent.text = noticeDatas[position].message

        // 시간은 나중에 날짜, 시간 계산 해야된다~
//        holder.noticeDateTime.text = noticeDatas[position].datetime
    }
}
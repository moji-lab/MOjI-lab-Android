package com.mojilab.moji.ui.main.mypage.notice.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class NoticeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var noticeProfileImg : ImageView = itemView!!.findViewById(R.id.iv_profile_notice_item)
    var noticeContent : TextView = itemView!!.findViewById(R.id.tv_content_name_notice_item)
    var noticeDefaultRl : RelativeLayout = itemView!!.findViewById(R.id.rl_default_proflleimg_notice)
    var noticeDateTime : TextView = itemView!!.findViewById(R.id.tv_datetime_notice)
    var noticeProfileNickName : TextView=itemView!!.findViewById(R.id.tv_name_notice_item)
}
package com.mojilab.moji.ui.main.mypage.adapter

import android.media.Image
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class MypageItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var profileImage : ImageView = itemView!!.findViewById(R.id.iv_profile_record)
    var profileName : TextView = itemView!!.findViewById(R.id.tv_name_record)
    var recordDate : TextView = itemView!!.findViewById(R.id.tv_date_record)
    var recordImagesRv : RecyclerView = itemView!!.findViewById(R.id.rv_image_record)

    var favoriteBtn : ImageButton = itemView!!.findViewById(R.id.btn_favorite_record)
    var chatBtn : ImageButton = itemView!!.findViewById(R.id.btn_chat_record)
    var scrabBtn : ImageButton = itemView!!.findViewById(R.id.btn_scrab_record)
    var moreBtn : ImageButton = itemView!!.findViewById(R.id.btn_more_record)
    var shareToggle: Switch = itemView!!.findViewById(R.id.switch_share_onoff_record)

    var coarse : TextView = itemView!!.findViewById(R.id.tv_coarse_record)
    var coarseContent : TextView = itemView!!.findViewById(R.id.tv_coarse_content_record)

    var likeNum : TextView = itemView!!.findViewById(R.id.tv_like_record)
    var commentNum : TextView = itemView!!.findViewById(R.id.tv_comment_record)
}
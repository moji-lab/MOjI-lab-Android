package com.mojilab.moji.ui.main.feed.DetailFeed.Comment

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData
import com.mojilab.moji.util.network.NetworkService

class DetailCommnetRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<DetailCommentData?>, var requestManager: RequestManager) :
    RecyclerView.Adapter<DetailCommnetRecyclerViewAdapter.Holder>() {

    lateinit var networkService : NetworkService

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(com.mojilab.moji.R.layout.rv_item_detail_comment, viewgroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_item_detail_comment_contents.text=dataList[position]!!.content
        holder.tv_itemt_detail_commnet_date.text=dataList[position]!!.writeTime!!.substring(0, 10)
        holder.tv_item_detail_comment_name.text=dataList[position]!!.userName
        // 프사 X일 때
        if(dataList[position]!!.profileImgUrl.equals("") || dataList[position]!!.profileImgUrl == null){
            holder.iv_item_profile_img.visibility = View.INVISIBLE
            holder.rl_defalut_proflie_comment.visibility = View.VISIBLE
            holder.tv_default_nickname_comment.visibility = View.VISIBLE
            // 닉네임 2글자 이상만 받도록 처리하자
            if(holder.tv_item_detail_comment_name.text.length >= 2){
                holder.tv_default_nickname_comment.text = holder.tv_item_detail_comment_name.text.substring(0,2)
            }

        }
        // 프사 O일때
        else{
            holder.iv_item_profile_img.visibility = View.VISIBLE
            holder.rl_defalut_proflie_comment.visibility = View.INVISIBLE
            holder.tv_default_nickname_comment.visibility = View.INVISIBLE
            requestManager.load(dataList[position]!!.profileImgUrl).into(holder.iv_item_profile_img)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_detail_comment_contents =itemView.findViewById(R.id.tv_item_detail_comment_contents) as TextView
        var tv_itemt_detail_commnet_date =itemView.findViewById(R.id.tv_itemt_detail_commnet_date) as TextView
        var tv_item_detail_comment_name=itemView.findViewById(R.id.tv_item_detail_comment_name) as TextView
        var iv_item_profile_img : ImageView = itemView.findViewById(R.id.cv_item_detail_comment_img) as ImageView
        var rl_defalut_proflie_comment : RelativeLayout = itemView.findViewById(R.id.rl_default_proflle_img_comment) as RelativeLayout
        var tv_default_nickname_comment : TextView = itemView.findViewById(R.id.tv_profile_name_comment)
    }
}

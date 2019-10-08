package com.mojilab.moji.ui.main.feed.DetailFeed.updatelock.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import com.bumptech.glide.RequestManager
import com.mojilab.moji.ui.main.mypage.data.PhotoData

class CourseUpdateAdapter(var context : Context, private var imageDatas: ArrayList<PhotoData>, var requestManager : RequestManager) : RecyclerView.Adapter<CourseUpdateViewHolder>(){

    var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseUpdateViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_update, parent, false)
        return CourseUpdateViewHolder(mainView)
    }

    override fun getItemCount(): Int = imageDatas.size

    override fun onBindViewHolder(holder: CourseUpdateViewHolder, position: Int) {

        //is대표사진
        if (position == 0) {
            holder.courseImg.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            holder.courseBoss.setVisibility(View.VISIBLE)
            holder.courseLock.setVisibility(View.GONE)
        } else {
            holder.courseBoss.setVisibility(View.GONE)
            holder.courseLock.setVisibility(View.VISIBLE)
        }

        requestManager.load(imageDatas[position].photoUrl).into(holder.courseImg)
        if(imageDatas[position].represent){
            holder.courseLock.isSelected = true
        }
        else{
            holder.courseLock.isSelected = false
        }
    }
}
package com.mojilab.moji.ui.main.feed.DetailFeed.updatelock.adapter

import android.content.ContentValues.TAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mojilab.moji.ui.main.mypage.data.PhotoData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.PostUpdateResponse
import retrofit2.Call
import retrofit2.Response

class CourseUpdateAdapter(var context : Context, private var imageDatas: ArrayList<PhotoData>, var requestManager : RequestManager, var courseIdx: String) : RecyclerView.Adapter<CourseUpdateViewHolder>(){

    var mContext = context
    var mCourseIdx = courseIdx

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

        //이미지
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(32))

        requestManager.load(imageDatas[position].photoUrl).apply(requestOptions).into(holder.courseImg)
        if(imageDatas[position].represent){
            holder.courseLock.isSelected = true
        }
        else{
            holder.courseLock.isSelected = false
        }

        //lock 상태
        holder.courseLock.setSelected(!imageDatas.get(position).represent)
        imageDatas.get(position).represent = holder.courseLock.isSelected()

        //lock버튼
        holder.courseLock.setOnClickListener(View.OnClickListener { view ->
            Log.v(TAG, "수정 락 클릭")
            //lock state 변경
            imageDatas.get(position).represent = !holder.courseLock.isSelected()
            holder.courseLock.setSelected(!holder.courseLock.isSelected())

            updateCourseImgOpen(mCourseIdx, position)
        })
    }
    // 코스 사진 공개/비공개 전환
    fun updateCourseImgOpen(courseIdx : String, index : Int){
        var networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(mContext)
        Log.v(TAG, "사진 공개/비공개 " + index + "번째 변경")
        val updateCourseImgOpenResponse = networkService.updateCourseImgOpen(token, courseIdx, index.toString())

        updateCourseImgOpenResponse.enqueue(object : retrofit2.Callback<PostUpdateResponse>{

            override fun onResponse(call: Call<PostUpdateResponse>, response: Response<PostUpdateResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 204){
                        if(response.body()!!.data){
                            Toast.makeText(mContext, "공개로 변경", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(mContext, "비공개로 변경", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PostUpdateResponse>, t: Throwable) {
            }
        })
    }
}
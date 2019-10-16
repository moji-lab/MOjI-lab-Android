package com.mojilab.moji.util.adapter
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderViewPager
import com.mojilab.moji.R
import com.mojilab.moji.data.PhotosData
import com.mojilab.moji.data.PostNoticeData
import com.mojilab.moji.ui.main.feed.DetailFeed.Comment.DetailCommentActivity
import com.mojilab.moji.ui.main.feed.DetailFeed.Comment.DetailCommnetRecyclerViewAdapter
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.CourseData
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.PhotoData
import com.mojilab.moji.ui.main.feed.DetailFeed.Tag.TagRecyclerViewAdapter
import com.mojilab.moji.util.bottomsheet.EditBottomsheetFragment
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetUserDataResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostCoarseLikeData
import kotlinx.android.synthetic.main.activity_detail_comment.*
import org.jetbrains.anko.ctx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class DetailFeedRecyclerViewAdapter(var activity: FragmentActivity, var ctx: Context, var dataList: ArrayList<CourseData?>, var userID : Int, var sameIdFlag : Int) :
    RecyclerView.Adapter<DetailFeedRecyclerViewAdapter.Holder>() {

    lateinit var networkService : NetworkService
    var mActivity = activity

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(com.mojilab.moji.R.layout.rv_item_detail_feed_course, viewgroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        var photos = ArrayList<PhotoData?>()
        for(i in 0 .. dataList[position]!!.course!!.photos.size-1){
            if(dataList[position]!!.course!!.photos.get(i)!!.represent){
                photos.add(dataList[position]!!.course!!.photos.get(i))
            }
        }
        // 마이 아이디 == 게시글 올린 유저 아이디
        if(sameIdFlag == 1){
            holder.btn_item_detail_add_more.visibility = View.VISIBLE
        }
        else{
            holder.btn_item_detail_add_more.visibility = View.GONE
        }


        var tagRecyclerViewAdapter = TagRecyclerViewAdapter(ctx, dataList[position]!!.course!!.tagInfo)
        holder.rv_item_detail_hashtag.adapter = tagRecyclerViewAdapter
        holder.rv_item_detail_hashtag.layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false)
        holder.tv_item_detail_place.text=dataList[position]!!.course!!.mainAddress
        holder.vp_item_viewpager.adapter=SliderAdapter(ctx, photos)
        holder.tv_item_detail_feed_visit_days.text=dataList[position]!!.course!!.visitTime.toString()
        holder.tv_item_detail_real_number.text=(position+1).toString()
        holder.tv_item_detail_smallheart_number.text=dataList[position]!!.likeCount.toString()
        holder.tv_item_detail_smallcomment_number.text=dataList[position]!!.course!!.comments.size.toString()

        if(dataList[position]!!.course!!.content.isNullOrEmpty()) {

        }else{
            holder.tv_item_detail_contente.text=dataList[position]!!.course!!.content
        }
        if(dataList[position]!!.liked==true){
            holder.ib_itemt_detail_favorite.isSelected=true
        }else{
            holder.ib_itemt_detail_favorite.isSelected=false
        }

        // 좋아요 버튼 이벤트
        holder.ib_itemt_detail_favorite.setOnClickListener {
            // 좋아요가 눌러 있다면
            if(holder.ib_itemt_detail_favorite.isSelected){
                holder.ib_itemt_detail_favorite.isSelected = false
                // 좋아요 -1 TextView 변경
                holder.tv_item_detail_smallheart_number.text = (Integer.parseInt(holder.tv_item_detail_smallheart_number.text as String) -1).toString()
            }
            // 좋아요가 눌러 있지 않다면
            else{
                holder.ib_itemt_detail_favorite.isSelected = true
                // 좋아요 +1 TextView 변경
                holder.tv_item_detail_smallheart_number.text = (Integer.parseInt(holder.tv_item_detail_smallheart_number.text as String) +1).toString()
                seondNotice()
            }
            coarseLike(position)
        }


        //댓글 창으로 이동
        holder.ib_itemt_detail_comment.setOnClickListener {
            var intent = Intent(ctx!!, DetailCommentActivity::class.java)
            intent.putExtra("randomFeedFlag", 2)

            intent.putExtra("coarseId", dataList[position]!!.course!!._id)
            intent.putExtra("flag", 1)
            intent.putExtra("userID", userID)
            ctx.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        }

        // 코스 사진 공개/비공개 설정 버튼 클릭시
        holder.btn_item_detail_add_more.setOnClickListener {
            val bottomSheetDialogFragment = EditBottomsheetFragment()
            val args = Bundle()
            args.putString("courseID", dataList[position]!!.course!!._id)
            bottomSheetDialogFragment.setArguments(args);
            bottomSheetDialogFragment.show(mActivity.supportFragmentManager, bottomSheetDialogFragment.tag)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_detail_feed_visit_days =itemView.findViewById(R.id.tv_item_detail_feed_visit_days) as TextView
        var tv_item_detail_real_number = itemView.findViewById(R.id.tv_item_detail_real_number) as TextView
        var tv_item_detail_place = itemView.findViewById(com.mojilab.moji.R.id.tv_item_detail_place) as TextView
        var vp_item_viewpager = itemView.findViewById(com.mojilab.moji.R.id.vp_item_viewpager) as CardSliderViewPager
        var ib_itemt_detail_favorite =itemView.findViewById(R.id.ib_itemt_detail_favorite) as ImageButton
        var ib_itemt_detail_comment =itemView.findViewById(R.id.ib_itemt_detail_comment) as ImageButton
        var tv_item_detail_contente = itemView.findViewById(com.mojilab.moji.R.id.tv_item_detail_contente) as TextView
        var tv_item_detail_smallheart_number = itemView.findViewById(com.mojilab.moji.R.id.tv_item_detail_smallheart_number) as TextView
        var tv_item_detail_smallcomment_number = itemView.findViewById(com.mojilab.moji.R.id.tv_item_detail_smallcomment_number) as TextView
        var rv_item_detail_hashtag = itemView.findViewById(R.id.rv_item_detail_hashtag) as RecyclerView
        var btn_item_detail_add_more = itemView.findViewById(R.id.btn_btn_more_item_detail) as ImageButton
    }


    // 좋아요
    fun coarseLike(position : Int) {
        var token : String = SharedPreferenceController.getAuthorization(ctx!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postCoarseLikeData = PostCoarseLikeData(dataList[position]!!.course!!._id!!)

        val postCoarseLikeResponse = networkService.postCoarseLike(token, postCoarseLikeData)
        postCoarseLikeResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }


    // 알림 보내기
    fun seondNotice() {
        var token : String = SharedPreferenceController.getAuthorization(ctx);
        var nickname : String = SharedPreferenceController.getUserNickname(ctx)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postNoticeData = PostNoticeData(userID, nickname + "님이 회원님의 게시물을 좋아합니다.")

        val postNoticeResponse = networkService.postNotice(token, postNoticeData)
        postNoticeResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                Log.v(TAG, "알림 데이터 = " + response.body().toString())
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "알림 메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

}
package com.mojilab.moji.util.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity
import com.mojilab.moji.ui.main.feed.SearchFeed.Course
import com.mojilab.moji.ui.main.home.HomeData.HomeFragmentResponse
import com.mojilab.moji.ui.main.home.HomeData.HomeRecyclerViewContentsData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetUserDataResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostFeedCommentData
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail_comment.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class SearchRecyclerViewAdapter(val ctx: Context, var dataList: ArrayList<Course>)
    : RecyclerView.Adapter<SearchRecyclerViewAdapter.Holder>(){
    var username=" "
    var img=" "
    lateinit var networkService : NetworkService
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_feed_searching_result, parent, false) // rv_item 수정 해야함
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //dataList[position].course!!._id//코스 아이디
       // getUserDataPost(dataList[position].course!!.userIdx!!)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val getHomeFragmentResponse = networkService.getUserData(dataList[position].course!!.userIdx!!)

        getHomeFragmentResponse.enqueue(object : retrofit2.Callback<GetUserDataResponse>{
            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                Toast.makeText(ctx,"피드 조회 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<GetUserDataResponse>?, response: Response<GetUserDataResponse>?) {
                if (response!!.isSuccessful) {
                    if(response.body()!!.status==200){
                        holder.tv_rv_search_name.text=response.body()!!.data.nickname
                        if(response.body()!!.data.photoUrl!! == null || response.body()!!.data.photoUrl!!.equals("") ){
                            holder.rl_default_proflle_img_search.visibility=View.VISIBLE
                            holder.tv_profile_name_search.text=username.substring(0,1)
                            // img=""
                        }else{
                            holder.rl_default_proflle_img_search.visibility=View.GONE
                            Glide.with(ctx)
                                .load(response.body()!!.data.photoUrl)
                                .into(holder.cv_rv_search_img)
                            // img= response.body()!!.data.photoUrl!!
                        }
                    }
                }
            }
        })
        holder.tv_rv_search_place.text=dataList[position].course!!.mainAddress

            Glide.with(ctx)
                .load(dataList[position].course!!.photos!![0]!!.photoUrl)
                .into(holder.image)

        holder.tv_rv_item_search_like_cnt.text=dataList[position].likeCount.toString()
        // 이미 좋아요 클릭했다면

        if(dataList[position].liked!!) holder.iv_rv_item_search_like_icon.isSelected = true
        else holder.iv_rv_item_search_like_icon.isSelected = false;
        if(dataList[position].likeCount!! >0) holder.iv_rv_item_search_like_icon.isSelected = true

   //     holder.tv_rv_search_name.text=username
     /*   if(img.equals("")||img==null){
            holder.rl_default_proflle_img_search.visibility=View.VISIBLE
            holder.tv_profile_name_search.text=username.substring(0,1)
        }else{
            Glide.with(ctx)
                .load(img)
                .into(holder.cv_rv_search_img)
        }*/

        holder.rl_image_directory_searching.setOnClickListener {
            //디테일 피드로 이동
            var intent = Intent(ctx, DetailFeedActivity::class.java)
            intent.putExtra("scrabFlag", 0)
            intent.putExtra("boardIdx", dataList[position].course!!.boardIdx)
            ctx.startActivity(intent)
        }
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tv_profile_name_search : TextView = itemView.findViewById(R.id.tv_profile_name_search) as TextView
        var rl_default_proflle_img_search : RelativeLayout= itemView.findViewById(R.id.rl_default_proflle_img_search) as RelativeLayout
        val iv_rv_item_search_like_icon :ImageView =itemView.findViewById(R.id.iv_rv_item_search_like_icon) as ImageView
        val tv_rv_search_name : TextView = itemView.findViewById(R.id.tv_rv_search_name) as TextView
        val tv_rv_item_search_like_cnt : TextView = itemView.findViewById(R.id.tv_rv_item_search_like_cnt) as TextView
        val tv_rv_search_place : TextView = itemView.findViewById(R.id.tv_rv_search_place) as TextView
        val image : ImageView= itemView.findViewById(R.id.iv_rv_search_img) as ImageView
        val cv_rv_search_img : CircleImageView = itemView.findViewById(R.id.cv_rv_search_img) as CircleImageView
        val rl_image_directory_searching : RelativeLayout = itemView.findViewById(R.id.rl_image_directory_searching) as RelativeLayout
    }
    fun getUserDataPost(isetIdx : Int){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val getHomeFragmentResponse = networkService.getUserData(isetIdx)

        getHomeFragmentResponse.enqueue(object : retrofit2.Callback<GetUserDataResponse>{
            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                Toast.makeText(ctx,"피드 조회 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<GetUserDataResponse>?, response: Response<GetUserDataResponse>?) {
                if (response!!.isSuccessful) {
                    if(response.body()!!.status==200){
                        username=response.body()!!.data.nickname
                        if(response.body()!!.data.photoUrl!!.equals("") || response.body()!!.data.photoUrl!! == null){
                         img=""
                        }else{
                            img= response.body()!!.data.photoUrl!!
                        }
                    }
                }
            }
        })
    }
}
package com.mojilab.moji.ui.main.feed.DetailFeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Response
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage.DetailFeedRecyclerViewData
import com.mojilab.moji.util.adapter.DetailFeedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_detail_feed.*
import android.widget.Toast
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage.FeedViewPagerData
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.GetDetailFeedResponse
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostScrapData
import retrofit2.Callback


class DetailFeedActivity : AppCompatActivity() {
    lateinit var networkService : NetworkService
    var dateRange : String = ""
    var boardId : String = ""
    val TAG = "DetailFeedActivity"

    lateinit var DetailFeedRecyclerViewAdapter: DetailFeedRecyclerViewAdapter
    var DetailFeedRecyclerViewDataList: ArrayList<DetailFeedRecyclerViewData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mojilab.moji.R.layout.activity_detail_feed)

        var boardIdx = intent.getStringExtra("boardIdx")

        getDetailfeed(boardIdx)
        iv_detail_feed_act_close_btn.setOnClickListener {
            finish()
        }
    }

    fun getDetailfeed(boardIdx : String){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(applicationContext)
//        val getdetailFeedResponse = networkService.getDetailFeedResponse(token,"5d73e104d6fc8b6dad41bf47")
        val getdetailFeedResponse = networkService.getDetailFeedResponse(token,boardIdx)

        getdetailFeedResponse.enqueue(object : retrofit2.Callback<GetDetailFeedResponse>{
            override fun onFailure(call: Call<GetDetailFeedResponse>, t: Throwable) {
                Toast.makeText(this@DetailFeedActivity,"실패",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<GetDetailFeedResponse>, response: Response<GetDetailFeedResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status==200){
                        var dataSize = response.body()!!.data!!.courseList.size
                        Glide.with(this@DetailFeedActivity).load(response.body()!!.data!!.user!!.photoUrl).into(cv_detail_feed_profile_image)
                        tv_detail_feed_city.text=response.body()!!.data!!.user!!.nickname
                        // 날짜 범위 조사
                        dateRange = response.body()!!.data!!.courseList[0]!!.course!!.visitTime + " ~ " + response.body()!!.data!!.courseList[dataSize-1]!!.course!!.visitTime
                        tv_detail_feed_visit_days.text = dateRange

                        // 보드 아이디 저장
                        boardId = response.body()!!.data!!._id!!
                        // 해당 피드 북마크 조사
                        if(response.body()!!.data!!.scraped!!) btn_detail_bookmark.isSelected = true
                        else btn_detail_bookmark.isSelected = false

                        // 북마크 버튼 이벤트
                        btn_detail_bookmark.setOnClickListener {
                            if(btn_detail_bookmark.isSelected){
                                btn_detail_bookmark.isSelected = false
                                deleteScrap()
                            }
                            else{
                                btn_detail_bookmark.isSelected = true
                                postScrap()
                            }
                        }

                        DetailFeedRecyclerViewAdapter = DetailFeedRecyclerViewAdapter(this@DetailFeedActivity, response.body()!!.data!!.courseList)
                        rv_detail_feed_contents.adapter = DetailFeedRecyclerViewAdapter
                        rv_detail_feed_contents.layoutManager = LinearLayoutManager(this@DetailFeedActivity,LinearLayoutManager.VERTICAL,false)

                    }else{

                    }

                }
            }
        })
    }


    // 북마크 ON
    fun postScrap() {
        var token : String = SharedPreferenceController.getAuthorization(applicationContext);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postScrapData = PostScrapData(boardId)

        val postSignupResponse = networkService.postScrap(token, postScrapData)
        postSignupResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
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

    // 북마크 OFF
    fun deleteScrap() {
        var token : String = SharedPreferenceController.getAuthorization(applicationContext);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postScrapData = PostScrapData(boardId)

        val postSignupResponse = networkService.deleteScrap(token, postScrapData)
        postSignupResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
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

}

package com.mojilab.moji.ui.main.feed.DetailFeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


class DetailFeedActivity : AppCompatActivity() {
    lateinit var networkService : NetworkService

    lateinit var DetailFeedRecyclerViewAdapter: DetailFeedRecyclerViewAdapter
    var DetailFeedRecyclerViewDataList: ArrayList<DetailFeedRecyclerViewData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mojilab.moji.R.layout.activity_detail_feed)
        getDetailfeed()
        iv_detail_feed_act_close_btn.setOnClickListener {
            finish()
        }
    }

    fun getDetailfeed(){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
       // var token : String = SharedPreferenceController.getAuthorization(applicationContext)
        val getdetailFeedResponse = networkService.getDetailFeedResponse("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb2ppIiwidXNlcl9JZHgiOjMxfQ.pQCy6cFP8YR_q2qyTTRfnAGT4WdEI_a_h2Mgz6HaszY","5d73e115d6fc8b6dad41bf4a")

        getdetailFeedResponse.enqueue(object : retrofit2.Callback<GetDetailFeedResponse>{
            override fun onFailure(call: Call<GetDetailFeedResponse>, t: Throwable) {
                Toast.makeText(this@DetailFeedActivity,"실패",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<GetDetailFeedResponse>, response: Response<GetDetailFeedResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status==200){
                        Glide.with(this@DetailFeedActivity).load(response.body()!!.data!!.user!!.photoUrl).into(cv_detail_feed_profile_image)
                        tv_detail_feed_city.text=response.body()!!.data!!.user!!.nickname
                        tv_detail_feed_visit_days.text=response.body()!!.data!!.writeTime.toString()
                        DetailFeedRecyclerViewAdapter = DetailFeedRecyclerViewAdapter(this@DetailFeedActivity, response.body()!!.data!!.courseList)
                        rv_detail_feed_contents.adapter = DetailFeedRecyclerViewAdapter
                        rv_detail_feed_contents.layoutManager = LinearLayoutManager(this@DetailFeedActivity,LinearLayoutManager.VERTICAL,false)

                    }else{

                    }

                }
            }
        })
    }

}

package com.mojilab.moji.ui.main.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.ui.login.LoginActivity
import com.mojilab.moji.ui.main.mypage.adapter.FeedItemAdapter
import com.mojilab.moji.ui.main.mypage.data.FeedData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetRandromFeedResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostLikeData
import kotlinx.android.synthetic.main.fragment_feed.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment()  {

    companion object{
        lateinit var feedFragment : FeedFragment
    }

    lateinit var recordAdapter : FeedItemAdapter
    lateinit var requestManager : RequestManager
    lateinit var networkService : NetworkService
    lateinit var myFeedDatas: ArrayList<FeedData>
    val TAG = "FeedFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_feed, container, false)
        feedFragment = this
        requestManager = Glide.with(this)

        setRecyclerview(v)
        return v;
    }

    fun setRecyclerview(v : View){

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(context!!)
        val getRandomFeedResonse = networkService.getRandomFeedResonse(token)

        getRandomFeedResonse.enqueue(object : retrofit2.Callback<GetRandromFeedResponse>{

            override fun onResponse(call: Call<GetRandromFeedResponse>, response: Response<GetRandromFeedResponse>) {
                if (response.isSuccessful) {
                    myFeedDatas = response.body()!!.data!!
                    Log.v(TAG, "랜덤피드 통신 성공 = " + myFeedDatas.toString())

                    // 피드 데이터가 있을 경우
                    if(myFeedDatas.size != 0){
                        recordAdapter = FeedItemAdapter(activity!!, context!!, myFeedDatas, requestManager)

                        v.rv_feed_content_feed.adapter = recordAdapter
                        v.rv_feed_content_feed.layoutManager = LinearLayoutManager(context)
                        v.rv_feed_content_feed.setNestedScrollingEnabled(false)
                    }
                }
                else{
                    Log.v(TAG, "통신 실패 = " + response.message().toString())
                }
            }
            override fun onFailure(call: Call<GetRandromFeedResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }
}
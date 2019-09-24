package com.mojilab.moji.ui.main.feed.DetailFeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage.DetailFeedRecyclerViewData
import com.mojilab.moji.ui.main.home.HomeData.HomeRecyclerViewContentsData
import com.mojilab.moji.util.adapter.DetailFeedRecyclerViewAdapter
import com.mojilab.moji.util.adapter.HomeContentsRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_detail_feed.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.ctx
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class DetailFeedActivity : AppCompatActivity() {
    lateinit var DetailFeedRecyclerViewAdapter: DetailFeedRecyclerViewAdapter
    var DetailFeedRecyclerViewDataList: ArrayList<DetailFeedRecyclerViewData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mojilab.moji.R.layout.activity_detail_feed)
        iv_detail_feed_act_close_btn.setOnClickListener {
            finish()
        }
        var img= ArrayList<String?>()
        var img2= ArrayList<String?>()
        var img3= ArrayList<String?>()
        img.add("https://t1.daumcdn.net/liveboard/dailylife/2b9ea3473a9e4e868b17bec5815983d5.jpg")
        img.add("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg")
        img.add("https://steemitimages.com/DQmZQcAQYCxHKLtomTLdjkMBqiAc5yZqq4dKwK6wZNvSeq4/44.jpg")
        img.add("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg")
        img.add("https://japan-magazine.jnto.go.jp/jnto2wm/wp-content/uploads/1506_fireworks_main.jpg")
        img.add("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg")
        img2.add("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg")
        img3.add("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg")
        img3.add("https://japan-magazine.jnto.go.jp/jnto2wm/wp-content/uploads/1506_fireworks_main.jpg")
        DetailFeedRecyclerViewDataList.add(DetailFeedRecyclerViewData("서울","2018년 10월 21일",img,"#SOPT","3","5"))
        DetailFeedRecyclerViewDataList.add(DetailFeedRecyclerViewData("인천","2019년 08월 11일",img2,"#먹방","2","5"))
        DetailFeedRecyclerViewDataList.add(DetailFeedRecyclerViewData("경기","2019년 07월 10일",img3,"#관광","1","5"))
        DetailFeedRecyclerViewAdapter = DetailFeedRecyclerViewAdapter(this, DetailFeedRecyclerViewDataList)
        rv_detail_feed_contents.adapter = DetailFeedRecyclerViewAdapter
        rv_detail_feed_contents.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }
}

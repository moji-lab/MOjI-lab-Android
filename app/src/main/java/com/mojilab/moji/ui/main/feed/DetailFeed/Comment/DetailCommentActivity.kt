package com.mojilab.moji.ui.main.feed.DetailFeed.Comment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData
import com.mojilab.moji.ui.main.feed.DetailFeed.Tag.TagRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_detail_comment.*
import org.jetbrains.anko.ctx

class DetailCommentActivity : AppCompatActivity() {
    lateinit var detailCommnetRecyclerViewAdapter: DetailCommnetRecyclerViewAdapter
    var DetailCommentDataList: ArrayList<DetailCommentData?> = ArrayList()
    val TAG = "DetailCommentActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comment)
        var intent = Intent()
        if(intent.getStringArrayExtra("content").isNullOrEmpty()){
            Log.v(TAG, "댓글 데이터 = null")
        }else{
            for (i in 0 ..intent.getIntExtra("size",0)-1){

                DetailCommentDataList.add(DetailCommentData(intent.getStringArrayExtra("content")[i],intent.getIntArrayExtra("userIdx")[i],intent.getStringArrayExtra("writeTime")[i]))
            }

            Log.v(TAG, "댓글 데이터 = " + DetailCommentDataList.toString())

            detailCommnetRecyclerViewAdapter = DetailCommnetRecyclerViewAdapter(ctx, DetailCommentDataList)
            rv_detail_comment.adapter = detailCommnetRecyclerViewAdapter
            rv_detail_comment.layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false)
        }

        iv_detail_comment_back_btn.setOnClickListener {
            finish()
        }

    }
}

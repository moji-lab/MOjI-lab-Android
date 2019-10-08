package com.mojilab.moji.ui.main.feed.DetailFeed.updatelock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.updatelock.adapter.CourseUpdateAdapter
import com.mojilab.moji.ui.main.mypage.data.PhotoData
import com.mojilab.moji.ui.main.upload.add.UploadImgRecyclerviewAdapter
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetCourseResponse
import com.mojilab.moji.util.network.post.PostResponse
import kotlinx.android.synthetic.main.activity_update_lock.*
import retrofit2.Call
import retrofit2.Response
import java.lang.StringBuilder

class UpdateLockActivity : AppCompatActivity() {

    lateinit var networkService : NetworkService
    lateinit var requestManager : RequestManager
    lateinit var courseUpdateAdapter : CourseUpdateAdapter
    val TAG = "UpdateLockActivity"
    private var imageDatas = ArrayList<PhotoData>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_lock)

        var courseID = intent.getStringExtra("courseID")
        Log.v(TAG, "update 받아온 코스 id = " + courseID)

        requestManager = Glide.with(this)
        getCourseData(courseID)

        iv_update_act_back_btn.setOnClickListener {
            finish()
        }
    }

    // 코스 상세 데이터 받아서 뷰에 씌우기
    fun getCourseData(courseIdx : String){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(applicationContext)
        val getCourseDataResponse = networkService.getCourseData(token, courseIdx)

        getCourseDataResponse.enqueue(object : retrofit2.Callback<GetCourseResponse>{

            override fun onResponse(call: Call<GetCourseResponse>, response: Response<GetCourseResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status==200){
                        Log.v(TAG, "코스 조회 성공 = " + response.body()!!.data.toString())

                        tv_update_act_write_location.text = response.body()!!.data.mainAddress
                        tv_update_act_select_date.text = response.body()!!.data.visitTime
                        tv_update_act_contents.text = response.body()!!.data.content

                        var sb : StringBuilder = StringBuilder()
                        for(i in 0 .. response.body()!!.data!!.tagInfo.size-1){
                            sb.append("#" + response.body()!!.data!!.tagInfo.get(i) + " ")
                        }
                        tv_update_act_tag.text = sb

                        imageDatas = response.body()!!.data.photos

                        courseUpdateAdapter = CourseUpdateAdapter(applicationContext, imageDatas, requestManager, courseIdx)
                        rv_update_act_img_list.adapter = courseUpdateAdapter
                        rv_update_act_img_list.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
            override fun onFailure(call: Call<GetCourseResponse>, t: Throwable) {
            }
        })
    }
}

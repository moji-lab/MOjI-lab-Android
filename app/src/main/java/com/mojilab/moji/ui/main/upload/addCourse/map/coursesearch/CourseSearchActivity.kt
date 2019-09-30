package com.mojilab.moji.ui.main.upload.addCourse.map.coursesearch

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mojilab.moji.R
import com.mojilab.moji.data.LocationData
import com.mojilab.moji.ui.main.upload.UploadActivity
import com.mojilab.moji.ui.main.upload.add.AddActivity
import com.mojilab.moji.ui.main.upload.addCourse.LocationRecyclerviewAdapter
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetAddressDataResponse
import kotlinx.android.synthetic.main.activity_course_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class CourseSearchActivity : AppCompatActivity() {

    lateinit var networkService : NetworkService
    lateinit var locationRecyclerviewAdapter: LocationRecyclerviewAdapter
    lateinit var locationDataArrayList: ArrayList<LocationData>
    val TAG = "CourseSearchActivity"
    lateinit var keyword : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_search)

        keyword = intent.getStringExtra("keyword")
        et_search_course_act_search_location.setText(keyword)
        getAddressData()

        btn_search_course_act_search_btn.setOnClickListener {
            keyword = et_search_course_act_search_location.text.toString()
            getAddressData()
        }

        iv_search_course_act_back_btn.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
       finish()
    }

    // 주소 검색 조회
    fun getAddressData() {
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val getAddressDataResponse =
            networkService.getAddressData(keyword)
        getAddressDataResponse.enqueue(object : Callback<GetAddressDataResponse> {
            override fun onResponse(
                call: Call<GetAddressDataResponse>,
                response: Response<GetAddressDataResponse>
            ) {
                if (response.body()!!.status == 200) {

                    locationDataArrayList = response.body()!!.data!!
                    Log.v(TAG, "Get Address Success = " + locationDataArrayList.toString())

                    // 데이터 값 없을 때
                    if (locationDataArrayList!!.size === 0) {
                        ll_search_course_act_help_comment.visibility = View.GONE
                        ll_search_course_act_rv_container.setVisibility(View.GONE)
                        ll_search_course_act_empty_container.setVisibility(View.VISIBLE)
                    } else {
                        ll_search_course_act_help_comment.visibility = View.GONE
                        Log.v(TAG, "주소 값 존재 = " + locationDataArrayList!![0].mainAddress)
                        ll_search_course_act_rv_container.setVisibility(View.VISIBLE)
                        ll_search_course_act_empty_container.setVisibility(View.GONE)

                        val mLinearLayoutManager = LinearLayoutManager(applicationContext)
                        rv_search_course_act_list.setLayoutManager(mLinearLayoutManager)

                        locationRecyclerviewAdapter =
                            LocationRecyclerviewAdapter(locationDataArrayList, applicationContext)
                        locationRecyclerviewAdapter.notifyDataSetChanged()
                        rv_search_course_act_list.setAdapter(locationRecyclerviewAdapter)

                        locationRecyclerviewAdapter.setOnItemClickListener(
                            LocationRecyclerviewAdapter.OnItemClickListener { v, position, mainAddress ->
                                if (intent.getIntExtra("add", 0) == 10) {
                                    val intent = Intent(applicationContext, AddActivity::class.java)
                                    intent.putExtra("main", mainAddress)
                                    setResult(Activity.RESULT_OK, intent)
                                } else {
                                    val intent =
                                        Intent(applicationContext, UploadActivity::class.java)
                                    intent.putExtra("main", mainAddress)
                                    setResult(Activity.RESULT_OK, intent)
                                }
                                finish()
                            })
                    }

                } else {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GetAddressDataResponse>, t: Throwable) {
                Log.v(TAG, "주소 검색 서버 연결 실패 = $t")
            }
        })
    }
}

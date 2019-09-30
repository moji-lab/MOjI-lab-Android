package com.mojilab.moji.ui.main.upload.addCourse.map.coarsename

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mojilab.moji.R
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostNewAddressData
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostLikeData
import kotlinx.android.synthetic.main.activity_coarse_name_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoarseNameRegisterActivity : AppCompatActivity() {

    lateinit var networkService : NetworkService
    val TAG : String = "NewAddressActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coarse_name_register)

        var receivedLat : Double = intent.getDoubleExtra("lat", 0.0);
        var receivedLng : Double = intent.getDoubleExtra("lng", 0.0);
        var coarseName : String = ""

        // 백버튼
        btn_back_name_register.setOnClickListener {
            finish()
        }

        // 위치 보러가기 버튼
        btn_back_map_name_register.setOnClickListener {
            finish()
        }

        // 완료 버튼
        btn_confirm_name_register.setOnClickListener{
            coarseName = edit_coarse_name_name_register.text.toString()
            Log.v(TAG, "장소명 = " + coarseName)
            if(coarseName.equals("")){
                Toast.makeText(applicationContext, "장소명을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else{
                postNewCoarse(coarseName, receivedLat, receivedLng)
            }
        }
    }

    // 새로운 주소 등록
    fun postNewCoarse(coarseName : String, receivedLat : Double, receivedLng : Double) {
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postNewAddressData : PostNewAddressData = PostNewAddressData(coarseName, receivedLat, receivedLng)

        val postNewAddressResponse = networkService.postNewAddress(postNewAddressData)
        postNewAddressResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "새로운 코스 등록 메시지 = " + response.body()!!.message)
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

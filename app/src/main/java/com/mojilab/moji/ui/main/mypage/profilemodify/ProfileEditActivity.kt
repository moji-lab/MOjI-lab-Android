package com.mojilab.moji.ui.main.mypage.profilemodify

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.MainActivity
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostLikeData
import com.mojilab.moji.util.network.put.PutProfieImgData
import kotlinx.android.synthetic.main.activity_profile_edit.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class ProfileEditActivity : AppCompatActivity() {

    private val REQ_CODE_SELECT_IMAGE = 100
    lateinit var data : Uri
    private var profileImage : MultipartBody.Part? = null

    lateinit var networkService: NetworkService
    lateinit var requestManager: RequestManager
    val TAG = "ProfileEditActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        var profileImg = intent.getStringExtra("profileImg")

        requestManager = Glide.with(this)
        requestManager.load(profileImg).into(img_profile_profile_edit)

        // 프로필 이미지 변경 이벤트
        btn_edit_profile_edit.setOnClickListener {
            changeImage()
        }

        // 일단 연결만 해놓은거
        tv_confirm_profile_edit.setOnClickListener {
            // 프로필 사진 변경 통신 시도
            updateProfileImg()
        }

        // Back 버튼과 같은 기능
        btn_back_profile_edit.setOnClickListener {
            setResult(28, intent)
            intent.putExtra("confirmFlag", 0);
            finish()
        }
    }

    // 유저 프로필 사진 변경
    fun updateProfileImg() {
        val token = SharedPreferenceController.getAuthorization(applicationContext)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        val updateProfileImgResponse = networkService.updateProfileImg(token, profileImage)

        updateProfileImgResponse.enqueue(object : retrofit2.Callback<PostResponse>{

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {

                Log.v(TAG, "응답 값 = " + response.body().toString())
                // 프로필 사진 변경 성공
                if(response.body()!!.status == 201){
                    Log.v(TAG, "프로필 사진 변경 성공")
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("confirmFlag", 1)
                    setResult(28, intent)
                    finish()
                }
                else{
                    Log.v(TAG, "서버 상태 코드 = " + response.body()!!.status)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable?) {
            }
        })
    }

    // 갤러리로부터 이미지 갖고올 때 사용하는 오버라이딩 메소드
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    this.data = data!!.data
                    val options = BitmapFactory.Options()

                    var input: InputStream? = null // here, you need to get your context.
                    try {
                        input = contentResolver.openInputStream(this.data)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(input, null, options) // InputStream 으로부터 Bitmap 을 만들어 준다.
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                    val img = File(getRealPathFromURI(applicationContext,this.data).toString()) // 가져온 파일의 이름을 알아내려고 사용합니다

                    profileImage = MultipartBody.Part.createFormData("profileImage", img.name, photoBody)

                    // 선택한 이미지를 해당 이미지뷰에 적용
                    Glide.with(this)
                        .load(data.data)
                        .into(img_profile_profile_edit)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    // 이미지 파일을 확장자까지 표시해주는 메소드
    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    // 방 배경 이미지 변경
    fun changeImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)
    }
}

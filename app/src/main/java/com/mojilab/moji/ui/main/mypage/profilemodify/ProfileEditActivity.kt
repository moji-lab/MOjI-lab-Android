package com.mojilab.moji.ui.main.mypage.profilemodify

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.MainActivity
import com.mojilab.moji.ui.main.mypage.MypageFragment
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
    val REQUEST_CODE_SELECT_IMAGE: Int = 1004
    val My_READ_STORAGE_REQUEST_CODE = 7777
    var selectedImageUri2: Uri?=null
    var imageURI: String? = null
    var tmp: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        var profileImg = SharedPreferenceController.getUserPicture(this)
        var nickname = intent.getStringExtra("nickname")
        Log.v(TAG, "프로필이미지 = "+ profileImg)
        if(profileImg == null||profileImg ==""){
            rl_default_proflle_img_profile_edit.visibility = View.VISIBLE
            btn_edit_profile_edit.visibility = View.GONE
            img_profile_profile_edit.visibility = View.GONE
            tv_profile_name_profile_edit.text = nickname
        }
        else{
            rl_default_proflle_img_profile_edit.visibility = View.GONE
            btn_edit_profile_edit.visibility = View.VISIBLE
            img_profile_profile_edit.visibility = View.VISIBLE
            requestManager = Glide.with(this)
            requestManager.load(profileImg).into(img_profile_profile_edit)
        }


        // 프로필 이미지 변경 이벤트
        btn_edit_profile_edit.setOnClickListener {
            requestReadExternalStoragePermission()
        }

        // 일단 연결만 해놓은거
        tv_confirm_profile_edit.setOnClickListener {
            // 프로필 사진 변경 통신 시도
            putChangeMyprofileResponse()
            finish()
        }

        // Back 버튼과 같은 기능
        btn_back_profile_edit.setOnClickListener {
            setResult(28, intent)
            intent.putExtra("confirmFlag", 0);
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(28, intent)
        intent.putExtra("confirmFlag", 0);
        finish()
    }
/*
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
//        val intent = Intent(Intent.ACTION_PICK)
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)
    }*/




    private fun requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//사용자에게 권한을 왜 허용해야되는지에 메시지를 주기 위한 대한 로직을 추가하려면 이 블락에서 하면됩니다!!
//하지만 우리는 그냥 비워놓습니다!! 딱히 줄말 없으면 비워놔도 무관해요!!! 굳이 뭐 안넣어도됩니다!
            } else {
//아래 코드는 권한을 요청하는 메시지를 띄우는 기능을 합니다! 요청에 대한 결과는 callback으로 onRequestPermissionsResult 메소드에서 받습니다!!!
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    My_READ_STORAGE_REQUEST_CODE
                )
            }
        } else {
//첫번째 if 문의 else 로써, 기존에 이미 권한 메시지를 통해 권한을 허용했다면 아래와 같은 곧바로 앨범을 여는 메소드를 호출 해주면됩니다!!
            showAlbum()
        }
    }

    //외부저장소(앨범과 같은)에 접근 관련 요청에 대해 OK를 했는지 거부했는지를 callback으로 받는 메소드입니다!
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//onActivityResult와 같은 개념입니다. requestCode로 어떤 권한에 대한 callback인지를 체크합니다.
        if (requestCode == My_READ_STORAGE_REQUEST_CODE) {
//결과에 대해 허용을 눌렀는지 체크하는 조건문이구요!
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//이곳은 외부저장소 접근을 허용했을 때에 대한 로직을 쓰시면됩니다. 우리는 앨범을 여는 메소드를 호출해주면되겠죠?
                showAlbum()
            } else {
//이곳은 외부저장소 접근 거부를 했을때에 대한 로직을 넣어주시면 됩니다.
                finish()
            }
        }
    }

    private fun showAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//REQUEST_CODE_SELECT_IMAGE를 통해 앨범에서 보낸 요청에 대한 Callback인지를 체크!!!
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
//undefinedData.data에는 앨범에서 선택한 사진의 Uri가 들어있습니다!! 그러니까 제대로 선택됐는지 null인지 아닌지를 체크!!!
                if (data != null) {
                    selectedImageUri2=data.data
                    val selectedImageUri: Uri = data.data
//Uri를 getRealPathFromURI라는 메소드를 통해 절대 경로를 알아내고, 인스턴스 변수 imageURI에 넣어줍니다!
                    imageURI = getRealPathFromURI(this,selectedImageUri)
                    Glide.with(this)
                        .load(selectedImageUri)
                        .thumbnail(0.1f)
                        .into(img_profile_profile_edit)
                    rl_edit_circle.setLayerType (View.LAYER_TYPE_SOFTWARE, null)
                    var strColor = "#111111";
                    tv_confirm_profile_edit.setTextColor(Color.parseColor(strColor))
                } else {
                    // 기본 이미지 url 등록 or src 이용 (서버에 null값 보내기)
                    /*imageURI = getRealPathFromURI(selectedImageUri)
                    Glide.with(this@ChangeProfileActivity)
                        .load(selectedImageUri)
                        .thumbnail(0.1f)
                        .into(civ_change_profile_pic)*/
                }
            }
        }
    }

    //Uri에 대한 절대 경로를 리턴하는 메소드입니다! 굳이 코드를 해석하려고 하지말고,
    //앱잼때 코드를 복붙을 통해 재사용해주세요!!
/*    fun getRealPathFromURI(content: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader: CursorLoader = CursorLoader(this, content, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_idx)
        cursor.close()
        return result
    }*/
    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            if(column_index.toString()!=""||column_index!=null)
            {
                return cursor.getString(column_index!!)
            }else return cursor.getString(1) //뭔지 모를 오류해결
        } finally {
            cursor?.close()
        }
    }
    private fun putChangeMyprofileResponse() {
        Log.e("write fail", "write post try")
        val token = SharedPreferenceController.getAuthorization(this)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

//아래 3줄의 코드가 이미지 파일을 서버로 보내기 위해 MultipartBody.Part 형식으로 만드는 로직입니다.
// imageURI는 앨범에서 선택한 이미지에 대한 절대 경로가 담겨있는 인스턴스 변수입니다.

            var profileImage: MultipartBody.Part? = null
            if (imageURI != null) {
                val file: File = File(imageURI)
                val requestfile: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), file)
                profileImage = MultipartBody.Part.createFormData("profileImage", file.name, requestfile)
            }
            val putChangeMyprofileResponse =
                networkService.updateProfileImg(token, profileImage)
            putChangeMyprofileResponse.enqueue(object : Callback<PostResponse> {
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileEditActivity,"알 수 없는 오류",Toast.LENGTH_SHORT).show()
                    Log.e("write fail", "write post fail")
                }

                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if (response.isSuccessful) {
                        Log.e("write fail", "write post sucees")
                        Log.v(TAG, "응답 값 = " + response.body().toString())
                        // 프로필 사진 변경 성공
                        if(response.body()!!.status == 201){
                            Toast.makeText(this@ProfileEditActivity,response.body()!!.message.toString(),Toast.LENGTH_SHORT).show()
                            Log.e("write fail", "write post sucees finish")
                            var intent = Intent(applicationContext, MypageFragment::class.java)
                            intent.putExtra("confirmFlag", 1)
                            setResult(28, intent)
                            SharedPreferenceController.clearUserPicture(this@ProfileEditActivity)
                            SharedPreferenceController.setUserPicture(this@ProfileEditActivity,selectedImageUri2.toString())
                            finish()
                        }
                        else{
                            Toast.makeText(this@ProfileEditActivity,response.body()!!.message.toString(),Toast.LENGTH_SHORT).show()
                            Log.v(TAG, "서버 상태 코드 = " + response.body()!!.status)
                        }
                    }
                }
            })

    }


}

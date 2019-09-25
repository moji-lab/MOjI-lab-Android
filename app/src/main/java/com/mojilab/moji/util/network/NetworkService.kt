package com.mojilab.moji.util.network

import com.mojilab.moji.data.LoginData
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.util.network.get.GetDuplicateCheckResponse
import com.mojilab.moji.util.network.get.GetNoticeDataResponse
import com.mojilab.moji.util.network.post.PostResponse
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    ////////////////////* GET *///////////////////////////
    // 이메일 중복체크
    @GET("/users/email/check?email={email}")
    fun getEmailDuplicateCheck(
        @Path("email") email : String
    ) : Call<GetDuplicateCheckResponse>

    // 닉네임 중복체크
    @GET("/users/nick/check?nickName={nickName}")
    fun getNicknameDuplicateCheck(
        @Path("nickname") nickname: String
    ) : Call<GetDuplicateCheckResponse>


    // 알림 가져오기
    @GET("/alarms")
    fun getNoticeData(
        @Header("token") token : String
    ) : Call<GetNoticeDataResponse>

    ////////////////////* POST *///////////////////////////
    // 회원가입
    @POST("/users")
    fun postSignup(
        @Body postRoom : SignupData
    ): Call<PostResponse>

    // 로그인
    @POST("/login")
    fun postLogin(
        @Body postLogin : LoginData
    ) : Call<PostResponse>
}
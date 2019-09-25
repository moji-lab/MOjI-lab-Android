package com.mojilab.moji.util.network

import com.mojilab.moji.data.LoginData
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.util.network.get.GetEmailDuplicateCheckResponse
import com.mojilab.moji.util.network.post.PostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkService {

    ////////////////////* GET *///////////////////////////
    // 이메일 중복체크
    @GET("/users/nick/check?nickName={nickName}")
    fun getEmailDuplicateCheck(
        @Path("nickName") nickName : String
    ) : Call<GetEmailDuplicateCheckResponse>

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
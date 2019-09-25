package com.mojilab.moji.util.network

import com.mojilab.moji.data.LoginData
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.util.network.post.PostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {

    ////////////////////* GET *///////////////////////////

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
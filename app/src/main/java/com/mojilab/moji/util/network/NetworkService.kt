package com.mojilab.moji.util.network

import com.mojilab.moji.data.SignupData
import com.mojilab.moji.util.network.post.PostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {

    ////////////////////* GET *///////////////////////////

    ////////////////////* POST *///////////////////////////
    // 새로운 약속 생성
    @POST("/users")
    fun postSignup(
        @Body postRoom : SignupData
    ): Call<PostResponse>
}
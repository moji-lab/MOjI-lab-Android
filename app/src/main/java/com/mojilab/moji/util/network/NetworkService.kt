package com.mojilab.moji.util.network

import com.mojilab.moji.data.LoginData
import com.mojilab.moji.data.PostHashTagsData
import com.mojilab.moji.data.PostNoticeData
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.GetDetailFeedResponse
import com.mojilab.moji.util.network.get.GetDuplicateCheckResponse
import com.mojilab.moji.util.network.get.GetNoticeDataResponse
import com.mojilab.moji.util.network.get.GetProfileImgResponse
import com.mojilab.moji.util.network.get.GetHashTagResponse
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

    // 프로필사진 가져오기
    @GET("/users/photoUrl/{userIdx}")
    fun getProfileImgUrl(
        @Path("userIdx") userIdx : String
    ) : Call<GetProfileImgResponse>

    // 해시태그 검색
    @GET("/hashtags")
    fun getHashTagResponse(
        @Query("tag") tag : String
    ) : Call<GetHashTagResponse>

    //Detail 피드 조회
    @GET("/boards/{boardIdx}")
    fun getDetailFeedResponse(
        @Header("Authorization") token : String,
        @Path("boardIdx") boardIdx : String
    ) : Call<GetDetailFeedResponse>

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

    // 알림
    @POST("/alarms")
    fun postNotice(
        @Header("token") token : String,
        @Body postNotice : PostNoticeData
    ) : Call<PostResponse>

    //해시태그 등록
    @POST("/hashtags")
    fun postHashTag(
        @Header("token") token : String,
        @Body postHashTags : PostHashTagsData
    ) : Call<PostResponse>
}
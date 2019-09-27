package com.mojilab.moji.util.network

import com.mojilab.moji.data.LoginData
import com.mojilab.moji.data.PostHashTagsData
import com.mojilab.moji.data.PostNoticeData
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.util.network.get.*
import com.mojilab.moji.util.network.post.PostResponse
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    ////////////////////* GET *///////////////////////////
    // 이메일 중복체크
    @GET("/users/email/check")
    fun getEmailDuplicateCheck(
        @Query("email") email : String
    ) : Call<GetDuplicateCheckResponse>

    // 닉네임 중복체크
    @GET("/users/nick/check")
    fun getNicknameDuplicateCheck(
        @Query("nickName") nickName: String
    ) : Call<GetDuplicateCheckResponse>

    // 알림 가져오기
    @GET("/alarms")
    fun getNoticeData(
        @Header("Authorization") token : String
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


    // 마이페이지 && 나의 기록 가져오기
    @GET("/mypage/1")
    fun getMypageRecordData(
        @Header("Authorization") token : String
    ) : Call<GetMypageRecordResponse>

    //친구 조회
    @GET("/shares/{person}")
    fun getFriendsTagResponse(
        @Header("token") token : String,
        @Path("person") person : String
    ) : Call<GetFriendsTagResponse>


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
        @Header("Authorization") token : String,
        @Body postNotice : PostNoticeData
    ) : Call<PostResponse>

    //해시태그 등록
    @POST("/hashtags")
    fun postHashTag(
        @Header("token") token : String,
        @Body postHashTags : PostHashTagsData
    ) : Call<PostResponse>
}
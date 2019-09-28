package com.mojilab.moji.util.network

import com.mojilab.moji.data.*
import com.mojilab.moji.util.network.get.*
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.GetDetailFeedResponse
import com.mojilab.moji.util.network.get.GetDuplicateCheckResponse
import com.mojilab.moji.util.network.get.GetNoticeDataResponse
import com.mojilab.moji.util.network.get.GetProfileImgResponse
import com.mojilab.moji.util.network.get.GetHashTagResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostLikeData
import com.mojilab.moji.util.network.post.data.PostScrapData
import com.mojilab.moji.util.network.put.PutProfieImgData
import okhttp3.MultipartBody
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

    //Detail 피드 조회
    @GET("/boards/{boardIdx}")
    fun getDetailFeedResponse(
        @Header("Authorization") token : String,
        @Path("boardIdx") boardIdx : String
    ) : Call<GetDetailFeedResponse>


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

    // 랜덤피드 조회
    @GET("/boards")
    fun getRandomFeedResonse(
        @Header("Authorization") token : String
    ) : Call<GetRandromFeedResponse>

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


    //게시물 등록
    @POST("/boards")
    fun postUpboard(
        @Header("Authorization") token : String,
        @Body postUpload : PostUploadData

    // 좋아요
    @POST("/likes/boards")
    fun postLike(
        @Header("Authorization") token : String,
        @Body postIdx : PostLikeData
    ) : Call<PostResponse>

       // 스크랩 ON
    @POST("/scrap")
    fun postScrap(
        @Header("Authorization") token : String,
        @Body postIdx : PostScrapData
    ) : Call<PostResponse>

    ////////////////////* PUT *///////////////////////////
    // 프로필 사진 수정
    @Multipart
    @PUT("/users/profile-image")
    fun updateProfileImg(
        @Header("Authorization") token : String,
        @Part profileImage : MultipartBody.Part?
    ) : Call<PostResponse>


    ////////////////////* DELETE *///////////////////////////
    // 스크랩 OFF
    @HTTP(method = "DELETE", path = "/scrap", hasBody = true)
    fun deleteScrap(
        @Header("Authorization") token : String,
        @Body postIdx : PostScrapData
    ) : Call<PostResponse>
}
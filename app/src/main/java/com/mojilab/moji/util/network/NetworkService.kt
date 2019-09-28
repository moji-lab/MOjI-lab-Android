package com.mojilab.moji.util.network

import com.mojilab.moji.data.*
import com.mojilab.moji.util.network.get.*
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.GetDetailFeedResponse
import com.mojilab.moji.ui.main.home.HomeData.HomeFragmentResponse
import com.mojilab.moji.util.network.get.GetDuplicateCheckResponse
import com.mojilab.moji.util.network.get.GetNoticeDataResponse
import com.mojilab.moji.util.network.get.GetProfileImgResponse
import com.mojilab.moji.util.network.get.GetHashTagResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.*
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

    // 나의 스크랩 가져오기
    @GET("/mypage/2")
    fun getMyScrapData(
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

    //홈 조회
    @GET("/home")
    fun getHomeFragmentResponse(
        @Header("Authorization") token : String
    ) : Call<HomeFragmentResponse>

    // 코스별 댓글 조회
    @GET("/comments/courses/{courseIdx}")
    fun getCoarseCommentResonse(
        @Header("Authorization") token : String,
        @Path("courseIdx") courseIdx : String
    ) : Call<GetCoarseCommentResponce>

    // 유저 정보 조회
    @GET("/users/{userIdx}")
    fun getUserData(
        @Path("userIdx") userIdx : String
    ) : Call<GetUserDataResponse>

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
        @Body postHashTags : PostHashTagsData
    ) : Call<PostResponse>

    //게시물 등록
    @POST("/boards")
    fun postUpboard(
        @Header("Authorization") token : String,
        @Body postUpload : PostUploadData
    ) : Call<PostResponse>

    // 피드 좋아요
    @POST("/likes/boards")
    fun postLike(
        @Header("Authorization") token : String,
        @Body postIdx : PostLikeData
    ) : Call<PostResponse>

    // 코스 좋아요
    @POST("/likes/courses")
    fun postCoarseLike(
        @Header("Authorization") token : String,
        @Body postIdx : PostCoarseLikeData
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

    // 피드 댓글 작성
    @POST("/comments/boards")
    fun postFeedComment(
        @Header("Authorization") token : String,
        @Body postIdx : PostRecordCommentData
    ) : Call<PostResponse>

    // 코스 댓글 작성
    @POST("/comments/courses")
    fun postCoarseComment(
        @Header("Authorization") token : String,
        @Body postIdx : PostCoarseCommentData
    ) : Call<PostResponse>


    ////////////////////* DELETE *///////////////////////////
    // 스크랩 OFF
    @HTTP(method = "DELETE", path = "/scrap", hasBody = true)
    fun deleteScrap(
        @Header("Authorization") token : String,
        @Body postIdx : PostScrapData
    ) : Call<PostResponse>
}
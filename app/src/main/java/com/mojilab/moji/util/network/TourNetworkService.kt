package com.mojilab.moji.util.network

import com.mojilab.moji.util.network.get.GetTourDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TourNetworkService {

    // 주변 관광지 데이터 가져오기
    @GET("rest/KorService/locationBasedList?ServiceKey=qRYEuZ2CaSIosY5zJByoD%2By9%2FIhLsZssGVEJCGeM39s%2FDAE1zlfzua79E3iWCak5t6k2dkT%2B01YNt7XUNSs7SQ%3D%3D")
    fun getTourData(
        @Query("numOfRows") numofRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("MobileOS") mobileOS : String,
        @Query("MobileApp") appName : String,
        @Query("mapX") lng : Double,
        @Query("mapY") lat : Double,
        @Query("radius") radius : Int,
        @Query("_type") type : String
        ) : Call<GetTourDataResponse>
}
package com.mojilab.moji.util.network.get

import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData
import com.mojilab.moji.ui.main.mypage.data.PhotoData

data class GetCourseData (
    var _id : String,
    var mainAddress : String,
    var subAddress : String,
    var visitTime : String,
    var content : String,
    var order : Int,
    var tagInfo : ArrayList<String>,
    var lat : Double,
    var lng : Double,
    var boardIdx : String,
    var userIdx : Int,
    var photos : ArrayList<PhotoData>,
    var comments : ArrayList<DetailCommentData>?
)
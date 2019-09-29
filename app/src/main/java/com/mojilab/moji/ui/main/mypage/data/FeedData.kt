package com.mojilab.moji.ui.main.mypage.data

data class FeedData (
    var userIdx : Int,
    var nickName : String,
    var profileUrl : String,
    var date : String,
    var boardIdx : String,
    var photoList : ArrayList<PhotoData>,
    var place : String,
    var likeCount : Int,
    var commentCount : Int,
    var mainAddress : String,
    var liked : Boolean,
    var scraped : Boolean
)


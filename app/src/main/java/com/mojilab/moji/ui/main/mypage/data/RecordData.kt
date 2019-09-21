package com.mojilab.moji.ui.main.mypage.data

data class RecordData (
    var profileImgUrl : String,
    var name : String,
    var date : String,
    var recordImg : ArrayList<String>?,
    var coarse : String,
    var carseContent : String,
    var likeNum : Int,
    var commentNum : Int
)

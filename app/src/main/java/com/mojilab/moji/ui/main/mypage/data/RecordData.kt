package com.mojilab.moji.ui.main.mypage.data

data class RecordData (
    var _id : String,
    var mainAddress : String?,
    var subAddress : String?,
    var writeTime : String,
    var open : Boolean,
    var userIdx : Int,
    var share : ArrayList<String>?,
    var comments : ArrayList<String>?
)


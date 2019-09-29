package com.mojilab.moji.data

data class NoticeData (
    var _id : String,
    var senderIdx : String,
    var senderPhotoUrl : String?,
    var message : String,
    var createTime : String
)
package com.mojilab.moji.util.network.post.data

data class ReturnLoginData (
    var token : String,
    var userIdx : Int,
    var profileUrl : String?,
    var nickname : String
)
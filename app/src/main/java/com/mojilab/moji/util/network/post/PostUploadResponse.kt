package com.mojilab.moji.util.network.post

data class PostUploadResponse (
    var status : Int,
    var message : String,
    var data : UploadResponse
)
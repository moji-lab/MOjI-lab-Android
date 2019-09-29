package com.mojilab.moji.util.network.post

import com.mojilab.moji.util.network.post.data.ReturnLoginData

data class PostLoginResponse (
    var status : Int,
    var message : String,
    var data : ReturnLoginData
)
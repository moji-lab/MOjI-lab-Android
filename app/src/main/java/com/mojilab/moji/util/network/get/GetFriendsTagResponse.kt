package com.mojilab.moji.util.network.get

import com.mojilab.moji.data.TagData

data class GetFriendsTagResponse (
    var status : Int,
    var message : String,
    var data : TagData?
)
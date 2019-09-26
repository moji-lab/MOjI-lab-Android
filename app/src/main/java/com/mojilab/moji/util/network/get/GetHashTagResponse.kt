package com.mojilab.moji.util.network.get

import com.mojilab.moji.data.HashTagData

data class GetHashTagResponse (
    var status : Int,
    var message : String,
    var data : ArrayList<HashTagData>?
)
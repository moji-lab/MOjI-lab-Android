package com.mojilab.moji.util.network.get

data class GetMypageRecordResponse (
    var status : Int,
    var message : String,
    var data : GetMypageRecordData
)
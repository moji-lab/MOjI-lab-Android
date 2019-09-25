package com.mojilab.moji.util.network.get

import com.mojilab.moji.data.NoticeData

data class GetNoticeDataResponse (
    var status : Int,
    var message : String,
    var data : ArrayList<NoticeData>
)
package com.mojilab.moji.util.network.get

import com.mojilab.moji.ui.main.mypage.data.RecordData

data class GetMypageRecordData (
    var nickname : String,
    var profileUrl : String,
    var boardCount : Int,
    var scrapCount : Int,
    var feedList : ArrayList<RecordData>
)
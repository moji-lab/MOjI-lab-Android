package com.mojilab.moji.util.network.get

import com.mojilab.moji.data.UserData

data class GetUserDataResponse (
    var status : Int,
    var message : String,
    var data : UserData
)
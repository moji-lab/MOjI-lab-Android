package com.mojilab.moji.util.network.get

import com.mojilab.moji.data.LocationData

data class GetAddressDataResponse (
    var status : Int,
    var message : String,
    var data : ArrayList<LocationData>?
)
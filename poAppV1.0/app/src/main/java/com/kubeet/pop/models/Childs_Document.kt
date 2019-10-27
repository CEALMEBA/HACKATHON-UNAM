package com.kubeet.models

import java.sql.Driver

data class Childs_Document(
    var address: String = "",
    var childCode: String = "",
    var description: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var name: String = "",
    var userId: String = "",
    var warehouseid: String = ""
)
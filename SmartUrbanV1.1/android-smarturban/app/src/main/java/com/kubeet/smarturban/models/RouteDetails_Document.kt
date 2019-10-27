package com.kubeet.models

import java.sql.Driver

data class RouteDetails_Document(

    var color: String = "",
    var description: String = "",
    var img: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var routeid: String = "",
    var userid: String = ""
)
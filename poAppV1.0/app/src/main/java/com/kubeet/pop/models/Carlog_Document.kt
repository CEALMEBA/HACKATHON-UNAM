package com.kubeet.models

import java.sql.Driver

data class Carlog_Document(
    var carid: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var status: String = "",
    var userid: String = ""
)
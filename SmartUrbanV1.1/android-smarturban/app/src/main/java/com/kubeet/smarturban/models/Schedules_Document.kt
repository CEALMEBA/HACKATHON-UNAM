package com.kubeet.models

import java.sql.Driver

data class Schedules_Document(
    var branchid: String = "",
    var dayweek: Int = 0,
    var employeeId: String = "",
    var endtime: String = "",
    var eventname: String = "",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0,
    var periodid: String = "",
    var radio: Int = 0,
    var starttime: String = "",
    var status: Int = 0,
    var tolerancia: Int = 0

)
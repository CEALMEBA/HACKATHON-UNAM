package com.kubeet.models

import java.sql.Driver

data class TicketPrices_Document(
    var price: Int = 0,
    var source: String = "",
    var target: String = "",
    var userid: String = ""
)
package com.kubeet.models

import java.sql.Driver

data class User_Document(
    var age: Int = 0,
    var avatar : String = "",
    var name: String = "",
    var nameToSearch: String = "",
    var surname: String = ""

)
package com.kubeet.models

import java.io.StringReader

class sessionStorage {

    companion object {

        var key: String = ""
        var carId: String = ""
        var userId: String = ""
        var price: Int = 0
        var source: String = ""
        var target: String = ""
        var latitud: Double = 0.0
        var longitud: Double = 0.0
        var velocidad: Double = 0.0
        var routeId: String = ""
        var routeName: String =""

        fun getkey(): String{
            return  key
        }

        fun getcarId(): String{
            return carId
        }

        fun getuserId(): String{
            return userId
        }

        fun getprice(): Int{
            return price
        }

        fun getsource(): String{
            return source
        }

        fun gettarget(): String{
            return target
        }

        fun getlatitud(): Double{
            return latitud
        }

        fun getlongitud(): Double{
            return longitud
        }

        fun getvelocidad(): Double{
            return  velocidad
        }

        fun getrouteId(): String{
            return routeId
        }

        fun getrouteName(): String{
            return routeName
        }
    }

    constructor( ){
        key = key
        carId = carId
        userId = userId
        price = price
        source = source
        target = target
        latitud = latitud
        longitud = longitud
        velocidad = velocidad
        routeId = routeId
        routeName = routeName
    }
}
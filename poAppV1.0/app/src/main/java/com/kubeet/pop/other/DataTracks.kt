package com.kubeet.pop.other

class DataTracks {

    companion object {

         var lat: Double= 0.00
         var long : Double= 0.00
         var info : String="mensaje desde datatraks"
         var userID : String=""
        var clienteID : String=""

        fun getLatitud():Double{
            return lat
        }


        fun getLongitud():Double{
            return long
        }


        fun getMessage():String{
            return info
        }


        fun getUserId():String{
            return userID
        }

        fun getClienteId():String{
            return clienteID
        }

    }

    constructor( latValue:Double,longValue:Double,infoValue:String,UserID:String,ClienteID:String){
        lat = latValue
        long = longValue
        info = infoValue
        userID = UserID
        clienteID = ClienteID


    }
}
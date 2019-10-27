package com.kubeet.models

import com.google.zxing.qrcode.encoder.QRCode
import java.io.StringReader
import java.util.*

class sessionStorage {

    companion object {

        var childCode: String = ""
        var price: Int = 0
        var type: String = ""
        var typeRecibo: String = ""
        var popUserId: String = ""
        var userName: String = ""
        var userId: String = ""
        var dateWallet: String = ""
        var mountWallet: String = ""
        var docKey: String = ""
        var cupoUserId: String = ""
        var sucId: String = ""
        var productPromo: String = ""
        var nameSuc: String = ""
        var userTravel: String = ""

        fun getchildCode(): String{
            return childCode
        }

        fun getprice(): Int{
            return price
        }

        fun gettype(): String{
            return type
        }

        fun getpopUserId(): String{
            return popUserId
        }

        fun getuserName(): String{
            return userName
        }

        fun getuserId(): String{
            return userId
        }

        fun getdateWallet(): String{
            return dateWallet
        }

        fun getmountWallet(): String{
            return mountWallet
        }

        fun gettypeRecibo(): String{
            return typeRecibo
        }

        fun getdocKey(): String{
            return docKey
        }

        fun getcuponUserId(): String{
            return cupoUserId
        }

        fun getsucId(): String{
            return sucId
        }

        fun getproductPromo(): String{
            return productPromo
        }

        fun getnameSuc(): String{
            return nameSuc
        }

        fun getuserTravel(): String{
            return userTravel
        }

    }

    constructor( ) {
        childCode = childCode
        price = price
        type = type
        popUserId = popUserId
        userName = userName
        userId = userId
        dateWallet = dateWallet
        mountWallet = mountWallet
        typeRecibo = typeRecibo
        docKey = docKey
        sucId = sucId
        productPromo = productPromo
        nameSuc = nameSuc
        userTravel = userTravel
    }
}
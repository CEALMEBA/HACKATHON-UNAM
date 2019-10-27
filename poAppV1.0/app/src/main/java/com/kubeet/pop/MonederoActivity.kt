package com.kubeet.pop

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.hussein.startup.R
import com.kubeet.models.Wallets_Document
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import kotlinx.android.synthetic.main.confirm_dialog_error.view.*
import kotlinx.android.synthetic.main.fragment_cupones.view.*
import kotlinx.android.synthetic.main.signup.*
import java.text.SimpleDateFormat
import java.util.*

class MonederoActivity : AppCompatActivity() {

    lateinit var  mlistView : ListView
    var  ListCupons = ArrayList<Wallets_Document>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_monedero)


        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setHomeAsUpIndicator(R.drawable.return_page)

        mlistView = findViewById<ListView>(R.id.listActiv) as ListView

        callWallets()

    }

    fun callWallets(){

        val db = FirebaseFirestore.getInstance()
        db.collection("wallets")
                .whereEqualTo("popuserid", sessionStorage.popUserId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getDocum ${document.id} => ${document.data}")

                        val callWallets = document.toObject(Wallets_Document::class.java)

                        println("printChild ${callWallets.mount}")

                        ListCupons.add(
                                Wallets_Document(
                                        childcode = callWallets.childcode,
                                        datemov = callWallets.datemov,
                                        mount = callWallets.mount,
                                        partnerid = callWallets.partnerid,
                                        tipomov = callWallets.tipomov,
                                        userid = callWallets.userid
                                )
                        )

                        callAdpater()
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getDocuments")
                }
    }


    private fun callAdpater() {

        var myNotesAdapter= MyNotesAdpater(this, ListCupons)
        mlistView.adapter=myNotesAdapter

        println("printAdapter ${myNotesAdapter.count}")

    }


    inner class  MyNotesAdpater:BaseAdapter{
        var listNotesAdpater=ArrayList<Wallets_Document>()
        var context:Context?=null
        constructor(context:Context, listNotesAdpater:ArrayList<Wallets_Document>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {


            var myView=layoutInflater.inflate(R.layout.fragment_cupones,null)
            var myNote=listNotesAdpater[p0]

            myView.txtNameSuc.text = myNote.childcode
            myView.txtPrice.text = "$ "+myNote.mount
            myView.txtCity.text = myNote.tipomov
            myView.txtDate.text = myNote.datemov



            println("printTest ${myNote.childcode}")


            myView!!.setOnClickListener {

                sessionStorage.typeRecibo = myNote.tipomov
                sessionStorage.mountWallet = myNote.mount.toString()
                sessionStorage.dateWallet = myNote.datemov
                sessionStorage.cupoUserId = myNote.userid
                println("printUser ${myNote.userid}")
                sessionStorage.sucId = myNote.childcode

                openRecibo()

            }

            return myView
        }



        override fun getItem(p0: Int): Any {



            return listNotesAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {

            return listNotesAdpater.size

        }


    }


    fun openRecibo() {
        try {
            var intent= Intent(baseContext, ReciboActivity::class.java)
            startActivity(intent)


            println("printMount ${sessionStorage.mountWallet}")
/*
            var registroFragment = SoporteViajePruebasFragment()
            fragmentManager!!.beginTransaction()
                    .replace(R.id.id_fragment_sopviaje, registroFragment)
                    //.addToBackStack(null)
                    .commit()*/
        }catch (exe:Exception){
            println("error cambio de fragment $exe")

        }

    }


    override fun onSupportNavigateUp():Boolean {
        finish()
        return true
    }
}

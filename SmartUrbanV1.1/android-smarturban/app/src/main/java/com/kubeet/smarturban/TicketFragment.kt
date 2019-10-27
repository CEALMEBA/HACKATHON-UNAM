package com.kubeet.smarturban

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.hussein.startup.R
import com.kubeet.models.TicketPrices_Document
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.fragment_ticket.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

class TicketFragment : Fragment() {

    companion object {
        fun newInstance(): TicketFragment = TicketFragment()
    }

    lateinit var  mlistView : ListView
    var  ListTickets = ArrayList<TicketPrices_Document>()
    var scannedResult: String = ""

    override fun onSaveInstanceState(outState:Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("scannedResult", scannedResult)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        container!!.removeAllViews()
        val view = inflater!!.inflate(R.layout.fragment_home,container,false)

        mlistView = view.findViewById<ListView>(R.id.listTicket) as ListView

        callTickets()

        return view
    }


    fun callTickets(){

        val db = FirebaseFirestore.getInstance()
        db.collection("ticketprices")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getTickets ${document.id} => ${document.data}")

                        val callTickets = document.toObject(TicketPrices_Document::class.java)

                        println("printChild ${callTickets.price}")

                        ListTickets.add(
                                TicketPrices_Document(
                                        price = callTickets.price,
                                        source = callTickets.source,
                                        target = callTickets.target,
                                        userid = callTickets.userid
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

        var myNotesAdapter= MyNotesAdpater(context!!, ListTickets)
        mlistView.adapter=myNotesAdapter

        println("printAdapter ${myNotesAdapter.count}")
    }


    inner class  MyNotesAdpater: BaseAdapter {
        var listNotesAdpater= ArrayList<TicketPrices_Document>()
        var context: Context?=null
        constructor(context: Context, listNotesAdpater: ArrayList<TicketPrices_Document>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())

            var myView=layoutInflater.inflate(R.layout.fragment_ticket,null)
            var myNote=listNotesAdpater[p0]

            myView.btnTicket.text = "$ "+myNote.price.toString()
            myView.txtOrigen.text = myNote.source
            myView.txtDestino.text = myNote.target
            myView.txtvelocidad.text = sessionStorage.velocidad.toString()+"km/hr"
            myView.txtFH.text = date



            println("printTest ${myNote.price}")

            myView.btnTicket.setOnClickListener {

                sessionStorage.price = myNote.price
                sessionStorage.source = myNote.source
                sessionStorage.target = myNote.target
                RegisterTicket()
            }

            myView.btnCamera.setOnClickListener{
                IntentIntegrator.forSupportFragment(this@TicketFragment).initiateScan()

                    val integrator = IntentIntegrator(activity)
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                    integrator.setPrompt("Scan A barcode or QR Code")
                    integrator.setCameraId(1)
                    integrator.setBeepEnabled(false)
                    integrator.setBarcodeImageEnabled(false)
                    integrator.initiateScan()

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

    fun RegisterTicket(){

        var everyDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())

        val db = FirebaseFirestore.getInstance()
        val city = hashMapOf(

                "carid" to sessionStorage.carId,
                "date" to everyDate,
                "driverid" to sessionStorage.userId,
                "price" to sessionStorage.price,
                "routeid" to "iQjJ5p7S3jXegg0WWTMi",
                "source" to sessionStorage.source,
                "target" to sessionStorage.target,
                "travelid" to "20180704-15",
                "userid" to "oBFp51RhsCpBrgwBNjLr",
                "latitude" to sessionStorage.latitud,
                "longitude" to sessionStorage.longitud
        )
        db.collection("ticketsales").add(city)
                .addOnSuccessListener {
                    println("add Information") }
                .addOnFailureListener { e ->
                    println("Error writing document $e") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null){
            if(result.contents != null){
                scannedResult = result.contents
                Toast.makeText(context, scannedResult, Toast.LENGTH_LONG ).show()
                RegisterQRCode()
            } else {
                Toast.makeText(context, "scan failed", Toast.LENGTH_LONG ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun RegisterQRCode() {

        val db = FirebaseFirestore.getInstance()
        val city = hashMapOf(

                "QRCode" to scannedResult
        )
        db.collection("wallets").add(city)
                .addOnSuccessListener {
                    println("add Information") }
                .addOnFailureListener { e ->
                    println("Error writing document $e") }
    }

}

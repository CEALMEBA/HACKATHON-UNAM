package com.kubeet.smarturban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.hussein.startup.R
import com.kubeet.models.sessionStorage

class ScanCode : AppCompatActivity() {

    /*********************************SCANCODE**********************************************/
    var scannedResult: String = ""
    lateinit var value: TextView
    lateinit var Scan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_code)


        value = findViewById(R.id.txtValue) as TextView
        Scan = findViewById(R.id.btnScan) as Button

        Scan.setOnClickListener {
            run {
                IntentIntegrator(this@ScanCode).initiateScan()

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                value.text = scannedResult
                RegisterQRCode()
            } else {
                value.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult")
            value.text = scannedResult
        }

    }



/**************************************FINISH SCAN CODE******************************************************/

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

package com.kubeet.pop

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.hussein.startup.R
import com.kubeet.models.Marketing_Documents
import com.kubeet.models.Wallets_Document
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReciboActivity : AppCompatActivity() {

    lateinit var cupon: TextView
    lateinit var date: TextView
    lateinit var mount: TextView
    lateinit var totalMount: TextView

    /*0001AH9415*/

    internal var bitmap: Bitmap? = null
    //private var etqr: EditText? = null
    private var iv: ImageView? = null
    private var btn: Button? = null

    var  ListRecibo = ArrayList<Wallets_Document>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recibo)


        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setHomeAsUpIndicator(R.drawable.return_page)


        iv = findViewById(R.id.iv) as ImageView
        //etqr = findViewById(R.id.etqr) as EditText
        btn = findViewById(R.id.btn) as Button


        cupon = findViewById(R.id.txtCupon) as TextView
        date = findViewById(R.id.txtDateCup) as TextView
        mount = findViewById(R.id.txtMountCup) as TextView
        totalMount = findViewById(R.id.txtTotalMount) as TextView



        btn!!.setOnClickListener {
            //if (etqr!!.text.toString().trim { it <= ' ' }.length == 0) {
              //  Toast.makeText(this@ReciboActivity, "Enter String!", Toast.LENGTH_SHORT).show()
            //} else {
                try {
                bitmap = TextToImageEncode("$${sessionStorage.mountWallet} | "+" ${sessionStorage.typeRecibo} | "+" ${sessionStorage.dateWallet} | "+" ${sessionStorage.popUserId} | "+" ${sessionStorage.cupoUserId} | "+" ${sessionStorage.sucId}" )
                    iv!!.setImageBitmap(bitmap)
                    val path = saveImage(bitmap)  //give read write permission
                    Toast.makeText(this@ReciboActivity, "QRCode saved to -> $path", Toast.LENGTH_SHORT).show()
                } catch (e: WriterException) {
                    e.printStackTrace()
                    println("errorQRCode $e")
                }

            //}
        }
        callWS()
    }



    fun callWS(){
        println("printStorageMount ${sessionStorage.typeRecibo}")
        cupon.text = sessionStorage.typeRecibo
        date.text = sessionStorage.dateWallet
        mount.text = "$ "+sessionStorage.mountWallet
        totalMount.text = "$ "+sessionStorage.mountWallet

    }

    fun saveImage(myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val wallpaperDirectory = File(
                Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg")
            f.createNewFile()   //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""

    }


    private fun TextToImageEncode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }

        val bitMatrixWidth = bitMatrix.getWidth()

        val bitMatrixHeight = bitMatrix.getHeight()

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    resources.getColor(R.color.penRoyalBlue)
                else
                    resources.getColor(R.color.white)
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    companion object {

        val QRcodeWidth = 500
        private val IMAGE_DIRECTORY = "/QRcodeDemonuts"
    }

    override fun onSupportNavigateUp():Boolean {
        finish()
        return true
    }



}

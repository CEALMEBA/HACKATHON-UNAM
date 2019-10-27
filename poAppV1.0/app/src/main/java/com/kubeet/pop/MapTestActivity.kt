package com.kubeet.pop

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.location.LocationManager
import android.os.Bundle
import android.support.design.R.id.add
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.hussein.startup.R
import com.kubeet.models.RouteDetail_Document
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import kotlinx.android.synthetic.main.confirm_dialog_error.view.*
import kotlinx.android.synthetic.main.signup.*
import java.text.SimpleDateFormat
import java.util.*

class MapTestActivity : AppCompatActivity() {



    var ListRouteDetails = ArrayList<RouteDetail_Document>()
    lateinit var locationManager: LocationManager
    lateinit var mapFragment: SupportMapFragment


    override fun onCreate(savedInstanceState: Bundle?) {

        println("in the activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        //setSupportActionBar(toolbar)


        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setHomeAsUpIndicator(R.drawable.return_page)

        callRouteDetails()
    }


    fun callRouteDetails() {
        try {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("routedetails")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                println("getRoutesd ${document.id} => ${document.data}")


                                val callRoutedetail = document.toObject(RouteDetail_Document::class.java)

                                ListRouteDetails.add(
                                        RouteDetail_Document(
                                                color = callRoutedetail.color,
                                                description = callRoutedetail.description,
                                                img = callRoutedetail.img,
                                                lat = callRoutedetail.lat,
                                                lng = callRoutedetail.lng,
                                                routeid = callRoutedetail.routeid,
                                                userid = callRoutedetail.userid
                                        )
                                )

                                println("countmyapp ${ListRouteDetails.count()}")

                                var latitude = callRoutedetail.lat
                                var longitude = callRoutedetail.lng

                                mapFragment.getMapAsync(object: OnMapReadyCallback {
                                    @SuppressLint("MissingPermission")
                                    override fun onMapReady(mMap: GoogleMap) {
                                        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                                        val googlePlex = CameraPosition.builder()
                                                .target(LatLng(latitude,longitude))
                                                .zoom(16F)
                                                .bearing(0F)
                                                .tilt(40F)
                                                .build()
                                        mMap.addMarker(MarkerOptions()
                                                .position(LatLng(latitude,longitude))
                                                .title(callRoutedetail.description)
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.markerbus,"", "ef2020")))
                                        )
                                    }
                                })
                            }
                        }
                        .addOnFailureListener { exception ->
                            println("Error getDocuments")
                        }
            } catch (ex: Exception) {
                println("$ex error de en mapa")
            }
        }
        catch (ex:Exception)
        {
            println("error fragmentPlanViaje $ex")
        }
    }




    private fun writeTextOnDrawable(drawableId: Int, text: String, color: String?): Bitmap {
        var bm: Bitmap? =null
        try{
            val r = Rect()
            bm = BitmapFactory.decodeResource(getResources(), drawableId)
                    .copy(Bitmap.Config.ARGB_8888, true)
            val tf = Typeface.create("Helvetica", Typeface.BOLD)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = Color.BLACK
            paint.typeface = tf
            paint.textAlign = Paint.Align.CENTER

            paint.textSize = convertToPixels(this!!, 15)
            val textRect = Rect()
            paint.getTextBounds(text, 0, text.length, textRect)
            val canvas = Canvas(bm)
            if (textRect.width() >= (canvas.width)-2)
                paint.textSize = convertToPixels(this!!, 4) //Scaling needs to be used for different dpi's
            val xPos = (canvas.width / 2)-r.top
            val yPos = ((canvas.height / 3) - ((paint.descent() + paint.ascent()) / 10))-r.top
            canvas.drawText(text, xPos.toFloat(), yPos, paint)
        }
        catch (ex:Exception){
            println("error writeTextOnDrawable $ex")
        }
        return bm!!
    }

    fun convertToPixels(context:Context, nDP:Int): Float {
        val conversionScale = context.resources.displayMetrics.density
        return ((nDP * conversionScale) + 0.4f).toInt().toFloat()
    }



    override fun onSupportNavigateUp():Boolean {
        finish()
        return true
    }




}

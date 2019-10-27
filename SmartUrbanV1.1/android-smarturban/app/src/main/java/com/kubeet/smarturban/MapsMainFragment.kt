package com.kubeet.smarturban

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.hussein.startup.R
import com.kubeet.models.*
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapsMainFragment : Fragment(), TextToSpeech.OnInitListener {

    lateinit var locationManager: LocationManager
    lateinit var mapFragment: SupportMapFragment

    var ListRouteDetail = ArrayList<RouteDetails_Document>()
    var latitud = 0.00
    var longitud = 0.00
    var bandera = true

    private var tts: TextToSpeech? = null
    private var swTrafico: Switch? = null
    private var btnSatelite: ImageButton? = null
    private var btnNormal: ImageButton? = null
    private var txtSatelite: TextView? = null
    private var txtPredeterminado: TextView? = null
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var wifiManager: WifiManager? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    companion object {
        fun newInstance(): MapsMainFragment = MapsMainFragment()
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("spa", "ESP"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {
            Toast.makeText(context, "ERROR TTS ..." , Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_map, container, false)

        //tts = TextToSpeech(getActivity(), this@MapsMainFragment as TextToSpeech.OnInitListener)
        tts = TextToSpeech(getActivity(),this)

        //btnSatelite = view.findViewById<ImageButton>(R.id.mapSatelite) as ImageButton
        //btnNormal = view.findViewById<ImageButton>(R.id.mapNormal) as ImageButton
        //swTrafico = view.findViewById<Switch>(R.id.swTrafico) as Switch
        //txtSatelite = view.findViewById<TextView>(R.id.txtSatelite) as TextView
        //txtPredeterminado = view.findViewById<TextView>(R.id.txtPredeterminado) as TextView

        //val bottomSheet = view.findViewById<LinearLayout>(R.id.bottom_sheet)

        //val behavior = BottomSheetBehavior.from(bottomSheet)

        wifiManager = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager


        mapFragment = getChildFragmentManager().findFragmentById(R.id.frg) as SupportMapFragment
        //Toast.makeText(activity, "mapa", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocation()
        } else {
            Toast.makeText(context, "No tiene permisos", Toast.LENGTH_LONG).show()
        }

        try {
            mapFragment.getMapAsync { mMap ->
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                // mMap.clear() //clear old markers
                val googlePlex = CameraPosition.builder()
                        .target(LatLng(latitud, longitud))
                        .zoom(18F)
                        .bearing(0F)
                        .tilt(40F)
                        .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)
            }

        } catch (ex: Exception) {
        }
        /*try {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // React to state change
                    Log.e("onStateChanged", "onStateChanged:" + newState)
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // React to dragging events
                    Log.e("onSlide", "onSlide")
                }
            })
            behavior.peekHeight = 200
        } catch (ex: Exception) {
            println("error en bootmSheet $ex")
        }*/

/*

        btnSatelite!!.setOnClickListener {

            btnSatelite!!.setBackgroundResource(R.drawable.satelite_select)
            btnNormal!!.setBackgroundResource(R.drawable.map_original)
            txtSatelite!!.setTextColor(Color.parseColor("#146ef9"))
            txtPredeterminado!!.setTextColor(Color.parseColor("#575756"))

            mapSatelite()
        }

        btnNormal!!.setOnClickListener {

            btnNormal!!.setBackgroundResource(R.drawable.map_original_select)
            btnSatelite!!.setBackgroundResource(R.drawable.satelite)
            txtSatelite!!.setTextColor(Color.parseColor("#575756"))
            txtPredeterminado!!.setTextColor(Color.parseColor("#146ef9"))
            mapNormal()
        }

        swTrafico!!.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                mapTrafico(true)
            } else {
                mapTrafico(false)
            }
        }
*/
        GetMapLocation()
        callRouteDetail()
        refreshPosition()
        //onInit(status = 1)

        return view
    }

    private fun mapTrafico(check: Boolean) {
        try {
            mapFragment.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(p0: GoogleMap?) {
                    p0!!.isTrafficEnabled = check
                }
            })
        } catch (ex: Exception) {
            println("error map trafico $ex")
        }


    }

    fun callRouteDetail(){

        val db = FirebaseFirestore.getInstance()
        db.collection("routedetails")
                .whereEqualTo("routeid", sessionStorage.routeId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getRoute ${document.id} => ${document.data}")
                        val callRoute = document.toObject(RouteDetails_Document::class.java)


                        ListRouteDetail.add(
                                RouteDetails_Document(
                                        color = callRoute.color,
                                        description= callRoute.description,
                                        img = callRoute.img,
                                        lat = callRoute.lat,
                                        lng = callRoute.lng,
                                        routeid = callRoute.routeid,
                                        userid = callRoute.userid
                                )
                        )



                        var latitude = callRoute.lat
                        var longitude = callRoute.lng

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
                                        .title(callRoute.description)
                                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.buslocation,"", "ef2020")))
                                )
                            }
                        })

                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getRoute")
                }
    }


    private fun writeTextOnDrawable(drawableId: Int, text: String, color: String?):Bitmap {

        var bm: Bitmap? =null
        try{
            //  val conf = Bitmap.Config.ARGB_8888
            //  val bm = Bitmap.createBitmap(200, 50, conf)
            val r = Rect()
            bm = BitmapFactory.decodeResource(getResources(), drawableId)
                    .copy(Bitmap.Config.ARGB_8888, true)
            val tf = Typeface.create("Helvetica", Typeface.BOLD)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = Color.BLACK
            paint.typeface = tf
            paint.textAlign = Paint.Align.CENTER


            paint.textSize = convertToPixels(context!!, 15)
            val textRect = Rect()
            paint.getTextBounds(text, 0, text.length, textRect)
            val canvas = Canvas(bm)
            //If the text is bigger than the canvas , reduce the font size
            if (textRect.width() >= (canvas.width)-2)
            //the padding on either sides is considered as 4, so as to appropriately fit in the text
                paint.textSize = convertToPixels(context!!, 4) //Scaling needs to be used for different dpi's
            //Calculate the positions

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

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            permissions

            if (hasGps) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location

                            latitud = locationGps!!.latitude
                            longitud = locationGps!!.longitude

                            if(bandera)
                                refresMap()

                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location



                            if(bandera)
                                refresMap()

                            latitud = locationNetwork!!.latitude
                            longitud = locationNetwork!!.longitude

                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    // tv_result.append("\nNetwork ")
                    // tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                    // tv_result.append("\nLongitude : " + locationNetwork!!.longitude)

                    latitud = locationNetwork!!.latitude
                    longitud = locationNetwork!!.longitude

                    if(bandera)
                        refresMap()



                }else{
                    if(bandera)
                        refresMap()


                    latitud = locationGps!!.latitude
                    longitud = locationGps!!.longitude


                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }


    }

    @SuppressLint("MissingPermission")
    private fun refresMap() {
        mapFragment.getMapAsync { mMap ->
            mMap!!.isMyLocationEnabled = true
            //  getUbication()
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
            mMap.clear() //clear old markers
            val googlePlex = CameraPosition.builder()
                    .target(LatLng(latitud,longitud))
                    .zoom(18F)
                    .bearing(0F)
                    .tilt(40F)
                    .build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null)

            sessionStorage.latitud = latitud
            sessionStorage.longitud = longitud
        }


        bandera=false
    }

    private fun mapSatelite() {

        mapFragment.getMapAsync(object: OnMapReadyCallback {
            @SuppressLint("MissingPermission")
            override fun onMapReady(mMap:GoogleMap) {

                //  getUbication()
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)

            }
        })


        bandera=false

    }

    private fun mapNormal() {
        mapFragment.getMapAsync(object: OnMapReadyCallback {
            @SuppressLint("MissingPermission")
            override fun onMapReady(mMap:GoogleMap) {

                //  getUbication()
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

            }
        })


        bandera=false

    }

    @SuppressLint("MissingPermission")
    fun GetMapLocation() {
        try {
            println("entry")
            permissions
            println("exit")

            var myLocation = MylocationListener()
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

            var myUpdateMap = RefreshMap()
            myUpdateMap.start()
        }catch (ex: Exception){
            println("errorLocation $ex")
        }
    }

    var isIn : Boolean = false
    var timer = 5
    var location:Location?=null

    inner class MylocationListener: LocationListener {
        constructor(){
            location=Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            location=p0
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun onProviderEnabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun onProviderDisabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    var myDist : Double = 0.0
    var oldLocation = Location(LocationManager.GPS_PROVIDER)
    var currentLocation = Location(LocationManager.GPS_PROVIDER)

    inner class RefreshMap: Thread {
        constructor() : super() {
        }
        override fun run() {
            while (true) {
                try {
                    /*if (oldLocation!!.distanceTo(location) == 0f)
                    {
                        continue
                    }
                    else {
                        currentLocation = oldLocation
                    }*/
                    currentLocation = oldLocation
                    oldLocation=location!!
                    getActivity()!!.runOnUiThread {
                        try
                        {
                            var distance = location!!.distanceTo(currentLocation).toDouble()

                            println(" current = " + currentLocation!!.latitude + " "+ currentLocation!!.longitude)
                            println(" old = " + location!!.latitude+" "+ location!!.longitude)
                            println("printdistance $distance")

                            var velocidad =  distance  / timer.toDouble()  // m/s

                            println("velocidad $velocidad")
                            println("timer $timer")
                            velocidad = velocidad * 3.6  // km/hr

                            if (velocidad < 2){
                                timer = 10
                            }

                            if ((velocidad > 2) && (velocidad < 20)){
                                timer = 30
                            }

                            if ((velocidad > 20) && (velocidad < 40)){
                                timer = 10
                            }

                            if ((velocidad > 40) && (velocidad < 60)){
                                timer = 5
                            }

                            if ((velocidad > 60) && (velocidad < 80)){
                                timer = 3
                            }

                            if ((velocidad > 80) && (velocidad < 100)){
                                timer = 2
                            }

                            if (velocidad > 100){
                                timer = 1
                            }

                            println("velocidadloc $velocidad")
                            println("timerloc $timer")
                            //Toast.makeText(context, "vel " +  velocidad + " -- distance = " + distance + "- timer "+ timer, Toast.LENGTH_SHORT).show()
                            println("boolean $isIn ")

                            var count = 0

                            ListRouteDetail.forEach {
                                count++
                                val locationpi = Location(LocationManager.GPS_PROVIDER)
                                locationpi!!.latitude= it.lat
                                locationpi!!.longitude = it.lng
                                try {
                                    val myRadio = 200
                                    if( location!!.distanceTo(locationpi) < myRadio!!){
                                        myDist = location!!.distanceTo(locationpi).toDouble()
                                        Toast.makeText(context, " dist .. " + myDist + " - " + isIn, Toast.LENGTH_LONG).show()
                                        println("boolean Entrando $isIn ")
                                        if ((isIn == false) && (myDist < (myRadio / 2))) {

                                            speakOut("Llegando a la parada de  ${it.description}")
                                            //speakOut("autobus a $velocidad kilometros")
                                            Toast.makeText(context, "Llegando a la parada de  ${it.description}", Toast.LENGTH_LONG).show()

                                            isIn = true
                                        }
                                        if ((isIn == true) && (myDist > (myRadio / 2))) {

                                            speakOut("Saliendo de la parada de ${it.description}")
                                            Toast.makeText(context, "Saliendo de la parada de ${it.description}", Toast.LENGTH_LONG).show()
                                            isIn = false
                                        }
                                    }
                                }catch (ex:Exception){
                                    //Toast.makeText(context, "Vel = " + ex,Toast.LENGTH_LONG).show()
                                    println("$ex error conditinal")
                                }
                            }
                        }
                        catch (ex:Exception)
                        {
                            println("error main menu tts $ex")
                        }
                    }
                    Thread.sleep( timer.toLong() * 1000)
                } catch(ex:Exception) {
                    println("error adsoft $ex")
                }
            }
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun refreshPosition(){
        var everyDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())
        val db = FirebaseFirestore.getInstance()
        val refresh = hashMapOf(
                "carid" to sessionStorage.carId,
                "lat" to latitud,
                "lng" to longitud,
                "status" to "2",
                "userid" to sessionStorage.userId,
                "routeid" to sessionStorage.routeId,
                "img" to "http://www.myiconfinder.com/uploads/iconsets/48-48-b54149c8a34605e62b415d7afa09ce47-bus.png",
                "registrationDay" to everyDate
        )
        db.collection("carlog").document(sessionStorage.key)
                .set(refresh)
                .addOnSuccessListener { println("DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> println("Error writing document $e") }
    }

    override fun setUserVisibleHint(isVisibleToUser:Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
        {
            val handler = Handler()
            handler.postDelayed({
                reloadFragment()
            }, 1000)
        }
    }

    private fun reloadFragment(){
        try{
            val ft = fragmentManager!!.beginTransaction()
            if (Build.VERSION.SDK_INT >= 26)
            {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()
        }
        catch (ex:Exception){
            println("error cambio fragment $ex")
        }
    }
}
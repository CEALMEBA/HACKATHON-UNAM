package com.kubeet.pop

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.RingtoneManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
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
import com.kubeet.pop.notification.NotificationService
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import org.jetbrains.anko.runOnUiThread
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val PERMISSION_REQUEST = 10

class MapsMainFragment : Fragment() {

    lateinit var locationManager: LocationManager
    lateinit var mapFragment: SupportMapFragment

    var ListChilds = ArrayList<Childs_Document>()
    var ListCarlog = ArrayList<Carlog_Document>()
    var ListMarketing = ArrayList<Marketing_Documents>()
    var ListCupon = ArrayList<Marketing_Documents>()
    var ListRouteDetails = ArrayList<RouteDetail_Document>()

    var latitud = 0.00
    var longitud = 0.00
    var bandera = true
    var soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    lateinit var builder: NotificationCompat.Builder

    private var mMap: GoogleMap? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_map, container, false)

        btnSatelite = view.findViewById<ImageButton>(R.id.mapSatelite) as ImageButton
        btnNormal = view.findViewById<ImageButton>(R.id.mapNormal) as ImageButton
        swTrafico = view.findViewById<Switch>(R.id.swTrafico) as Switch
        txtSatelite = view.findViewById<TextView>(R.id.txtSatelite) as TextView
        txtPredeterminado = view.findViewById<TextView>(R.id.txtPredeterminado) as TextView

        val bottomSheet = view.findViewById<LinearLayout>(R.id.bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)

        wifiManager = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        //mNotificationManager = getActivity()!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mapFragment = getChildFragmentManager().findFragmentById(R.id.frg) as SupportMapFragment
        //Toast.makeText(activity, "mapa", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocation()
        } else {
            Toast.makeText(context, "No tiene permisos", Toast.LENGTH_LONG).show()
        }
        try {
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
        }

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

        callWS()
        GetUserLocation()
        GetMapLocation()
        //callChilds();
        callRouteDetails()
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

    @SuppressLint("MissingPermission")
    fun callWS() {
        try {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("childs")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                println("getDocum ${document.id} => ${document.data}")


                                val callChilds = document.toObject(Childs_Document::class.java)

                                ListChilds.add(
                                        Childs_Document(
                                                address = callChilds.address,
                                                childCode = callChilds.childCode,
                                                description = callChilds.description,
                                                lat = callChilds.lat,
                                                lng = callChilds.lng,
                                                name = callChilds.name,
                                                userId = callChilds.userId,
                                                warehouseid = callChilds.warehouseid
                                        )
                                )

                                println("countmyapp ${ListChilds.count()}")

                                var latitude = callChilds.lat
                                var longitude = callChilds.lng

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
                                                .title(callChilds.name)
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.markerprom,"", "ef2020")))
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

    fun callRouteDetails() {
        try {
            try {
                println("printsesisonroute ${sessionStorage.userTravel}")
                val db = FirebaseFirestore.getInstance()
                db.collection("routedetails")
                        .whereEqualTo("userid", sessionStorage.userTravel)
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
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.bus,"", "ef2020")))
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

    private fun writeTextOnDrawable(drawableId: Int, text: String, color: String?):Bitmap {
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

        paint.textSize = convertToPixels(context!!, 15)
        val textRect = Rect()
        paint.getTextBounds(text, 0, text.length, textRect)
        val canvas = Canvas(bm)
        if (textRect.width() >= (canvas.width)-2)
            paint.textSize = convertToPixels(context!!, 4) //Scaling needs to be used for different dpi's
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
        }
        bandera=false
    }

    private fun mapSatelite() {
        mapFragment.getMapAsync(object: OnMapReadyCallback {
            @SuppressLint("MissingPermission")
            override fun onMapReady(mMap:GoogleMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
            }
        })
        bandera=false
    }

    private fun mapNormal() {
        mapFragment.getMapAsync(object: OnMapReadyCallback {
            @SuppressLint("MissingPermission")
            override fun onMapReady(mMap:GoogleMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
            }
        })
        bandera=false

    }

    fun callMarketing(sucid: String){
        val db = FirebaseFirestore.getInstance()
        db.collection("marketing")
                .whereEqualTo("sucid",sucid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getDocum ${document.id} => ${document.data}")

                        println("promocion $document")

                        val callMarketing = document.toObject(Marketing_Documents::class.java)

                        if (callMarketing.type == "promo"){
                        ListMarketing.add(
                                Marketing_Documents(
                                        description = callMarketing.description,
                                        imageurl = callMarketing.imageurl,
                                        key = callMarketing.key,
                                        price = callMarketing.price,
                                        productid = callMarketing.productid,
                                        quantity = callMarketing.quantity,
                                        status = callMarketing.status,
                                        sucid = callMarketing.sucid,
                                        type = callMarketing.type,
                                        userid = callMarketing.userid
                                )
                        )
                            sessionStorage.productPromo = callMarketing.description
                            createChannel("", 1)

                            //Toast.makeText(context, "promocion de ${callMarketing.description}", Toast.LENGTH_LONG).show()
                        }

                        if (callMarketing.type == "cupon"){
                        ListCupon.add(
                                Marketing_Documents(
                                        description = callMarketing.description,
                                        imageurl = callMarketing.imageurl,
                                        key = callMarketing.key,
                                        price = callMarketing.price,
                                        productid = callMarketing.productid,
                                        quantity = callMarketing.quantity,
                                        status = callMarketing.status,
                                        sucid = callMarketing.sucid,
                                        type = callMarketing.type,
                                        userid = callMarketing.userid
                                )
                        )

                            sessionStorage.price = callMarketing.price
                            sessionStorage.type = callMarketing.type

                            if (callMarketing.type == "cupon"){
                                val mDialogView = LayoutInflater.from(context).inflate(R.layout.confirm_dialog, null)
                                mDialogView.txtTitleDialog.text = "Oferta de un cupon"
                                mDialogView.txtDialog.text = "Hay un cupon de "

                                val mBuilder = AlertDialog.Builder(this!!.context!!)
                                        .setView(mDialogView)
                                val  mAlertDialog = mBuilder.show()
                                mDialogView.btnAceptar.setOnClickListener {
                                    addCupon()
                                    mAlertDialog.dismiss()
                                }
                                mDialogView.btnCancelar.setOnClickListener {
                                    mAlertDialog.dismiss()
                                }
                            }
                        }
                    }
                        }
                            .addOnFailureListener { exception ->
                                println("Error getDocuments")
                            }

    }

    @SuppressLint("MissingPermission")
    fun GetUserLocation() {
        try {
            println("entrando")
            permissions
            println("Saliendo")

            var myLocation = MylocationListener()
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

            var mythread = myThread()
            mythread.start()
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

    inner class myThread: Thread {
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
                                timer = 60
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

                            ListChilds.forEach {
                                count++
                                val locationpi = Location(LocationManager.GPS_PROVIDER)
                                locationpi!!.latitude= it.lat
                                locationpi!!.longitude = it.lng
                                try {
                                    val myRadio = 300
                                    if( location!!.distanceTo(locationpi) < myRadio!!){
                                        myDist = location!!.distanceTo(locationpi).toDouble()
                                        //Toast.makeText(context, " dist .. " + myDist + " - " + isIn, Toast.LENGTH_LONG).show()
                                        println("boolean Entrando $isIn ")
                                            if ((isIn == false) && (myDist < (myRadio / 2))) {
                                                //speakOut("Entrando puntual a ${it.branchid}")
                                                //Toast.makeText(context, "Promocion de ${sessionStorage.ProductDescription}", Toast.LENGTH_LONG).show()
                                                sessionStorage.childCode = it.childCode
                                                sessionStorage.nameSuc = it.name

                                                callMarketing(it.childCode)

                                                var userid = ListMarketing.first().userid
                                                sessionStorage.userId = userid

                                                isIn = true
                                            }
                                            if ((isIn == true) && (myDist > (myRadio / 2))) {
                                                //speakOut("Saliendo antes de ${it.branchid}")
                                                //Toast.makeText(context, "hola - adios", Toast.LENGTH_LONG).show()
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

    fun addCupon(){
        var everyDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())

        val db = FirebaseFirestore.getInstance()
        val register = hashMapOf(

                "childcode" to sessionStorage.childCode,
                "datemov" to everyDate,
                "mount" to sessionStorage.price,
                "partnerid" to "xxxxxxxxx",
                "tipomov" to sessionStorage.type,
                "popuserid" to sessionStorage.popUserId,
                "userid" to sessionStorage.userId
        )
        db.collection("wallets").add(register)
                .addOnSuccessListener {
                    println("add Information") }
                .addOnFailureListener { e ->
                    println("Error writing document $e") }

    }

/***********************************REFRESH MAP******************************************************/

    @SuppressLint("MissingPermission")
    fun GetMapLocation() {
        try {
            println("entry")
            permissions
            println("exit")
            var myLocation = MylocationListener()
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
            //var myUpdateMap = RefreshMap()
            //myUpdateMap.start()
        }catch (ex: Exception){
            println("errorLocation $ex")
        }
    }
    /*inner class RefreshMap: Thread {
        constructor() : super() {
        }
        override fun run() {
            while (true) {
                try {
                    currentLocation = oldLocation
                    oldLocation=location!!
                    getActivity()!!.runOnUiThread {
                        try
                        {
                            println("printCars")
                            try {
                                try {
                                    val db = FirebaseFirestore.getInstance()
                                    mapFragment.getMapAsync(object: OnMapReadyCallback {
                                        @SuppressLint("MissingPermission")
                                        override fun onMapReady(mMap: GoogleMap) {
                                            mMap.clear()
                                        }
                                    })

                                    callWS()
                                    db.collection("carlog")
                                            .get()
                                            .addOnSuccessListener { result ->
                                                for (document in result) {
                                                    println("getCar ${document.id} => ${document.data}")

                                                    val callCarlog = document.toObject(Carlog_Document::class.java)

                                                    //val builder = LatLngBounds.Builder()
                                                    ListCarlog.add(
                                                            Carlog_Document(
                                                                    carid = callCarlog.carid,
                                                                    lat = callCarlog.lat,
                                                                    lng = callCarlog.lng,
                                                                    status = callCarlog.status,
                                                                    userid = callCarlog.userid
                                                            )
                                                    )

                                                    var latitudeCar = callCarlog.lat
                                                    var longitudeCar = callCarlog.lng

                                                    mapFragment.getMapAsync(object: OnMapReadyCallback {
                                                        @SuppressLint("MissingPermission")
                                                        override fun onMapReady(mMap: GoogleMap) {
                                                            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                                                            val googlePlex = CameraPosition.builder()
                                                                    .target(LatLng(latitudeCar,longitudeCar))
                                                                    .zoom(16F)
                                                                    .bearing(0F)
                                                                    .tilt(40F)
                                                                    .build()
                                                            mMap.addMarker(MarkerOptions()
                                                                    .position(LatLng(latitudeCar,longitudeCar))
                                                                    .title(callCarlog.userid)
                                                                    .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.markerbus,"", "ef2020")))
                                                            )
                                                        }
                                                    })
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                println("Error getCar")
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
                        catch (ex:Exception)
                        {
                            println("error main menu tts $ex")
                        }
                    }
                    Thread.sleep( 10 * 1000)//timerMap.toLong() * 1000)
                } catch(ex:Exception) {
                    println("error adsoft $ex")
                }
            }
        }
    }*/

    /*override fun setUserVisibleHint(isVisibleToUser:Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
        {
            val handler = Handler()
            handler.postDelayed({
                //reloadFragment()
            }, 5000)
        }
    }*/

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

    private fun createChannel(notification: String,type:Int) {

        context!!.runOnUiThread(object:Runnable, (Context) -> Unit {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun invoke(p1: Context) {

                if (type == 0)
                {
                    val pendingIntent = PendingIntent.getActivity(context, 0, getActivity()!!.intent, PendingIntent.FLAG_UPDATE_CURRENT)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val detailsIntent = Intent(context, MapsMainFragment::class.java)
                        //   detailsIntent.putExtra("EXTRA_DETAILS_ID", codigo)
                        val detailsPendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                detailsIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )


                        var name = getString(R.string.notification_message)// The user-visible name of the channel.
                        var importance = NotificationManager.IMPORTANCE_HIGH

                        var mChannel = NotificationChannel(NotificationService.CHANNEL_ID, name, importance)

                        builder = NotificationCompat.Builder(context!!, NotificationService.CHANNEL_ID)
                                .setSmallIcon(R.drawable.notifi)
                                .setContentTitle("Asignacion de  viaje")
                                .setContentText("Viaje-" + notification)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setSound(soundUri)
                                .setAutoCancel(true)
                                .setContentIntent(detailsPendingIntent)


                        val notificationManager = getActivity()!!.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(mChannel)
                        notificationManager.notify(1, builder.build())


                    } else {

                        val detailsIntent = Intent(context, MapsMainFragment::class.java)
                        val detailsPendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                detailsIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        var name = getString(R.string.notification_message)// The user-visible name of the channel.
                        var importance = NotificationManager.IMPORTANCE_HIGH

                        var mChannel = NotificationChannel(NotificationService.CHANNEL_ID, name, importance)

                        builder = NotificationCompat.Builder(context!!, NotificationService.CHANNEL_ID)
                                .setSmallIcon(R.drawable.notifi)
                                .setContentTitle("")
                                .setContentText("" + notification)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setSound(soundUri)
                                .setAutoCancel(true)
                                .setContentIntent(detailsPendingIntent)

                        val notificationManager = getActivity()!!.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(mChannel)
                        notificationManager.notify(1, builder.build())
                    }
                }else{
                    val pendingIntent = PendingIntent.getActivity(context, 0, getActivity()!!.intent, PendingIntent.FLAG_UPDATE_CURRENT)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val detailsIntent = Intent(context, MapsMainFragment::class.java)
                        // detailsIntent.putExtra("EXTRA_DETAILS_ID", codigo)
                        val detailsPendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                detailsIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        var name = getString(R.string.notification_message)// The user-visible name of the channel.
                        var importance = NotificationManager.IMPORTANCE_HIGH
                        var mChannel = NotificationChannel(NotificationService.CHANNEL_ID, name, importance)

                        builder = NotificationCompat.Builder(context!!, NotificationService.CHANNEL_ID)
                                .setSmallIcon(R.drawable.notifi)
                                .setContentTitle("Hay una promocion cerca")
                                .setContentText("promocion de ${sessionStorage.productPromo}")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setSound(soundUri)
                                .setAutoCancel(true)
                                .setContentIntent(detailsPendingIntent)

                        val notificationManager = getActivity()!!.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(mChannel)
                        notificationManager.notify(123, builder.build())

                    } else {

                        val detailsIntent = Intent(context, MapsMainFragment::class.java)
                        val detailsPendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                detailsIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        var name = getString(R.string.notification_message)// The user-visible name of the channel.
                        var importance = NotificationManager.IMPORTANCE_HIGH
                        var mChannel = NotificationChannel(NotificationService.CHANNEL_ID, name, importance)

                        builder = NotificationCompat.Builder(context!!, NotificationService.CHANNEL_ID)
                                .setSmallIcon(R.drawable.notifi)
                                .setContentTitle("Hay una promocion cerca")
                                .setContentText("promocion de ${sessionStorage.productPromo}")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setSound(soundUri)
                                .setAutoCancel(true)
                                .setContentIntent(detailsPendingIntent)

                        val notificationManager = getActivity()!!.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(mChannel)
                        notificationManager.notify(123, builder.build())

                    }
                }
            }
            override fun run() {
            }
        })
    }

}
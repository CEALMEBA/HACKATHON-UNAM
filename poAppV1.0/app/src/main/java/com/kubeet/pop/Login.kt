package com.kubeet.pop

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hussein.startup.R
import com.kubeet.models.Employes_Document
import com.kubeet.models.Popusers
import com.kubeet.models.sessionStorage
import com.kubeet.pop.other.DataTracks
import com.kubeet.pop.other.DatabaseHandler
import kotlinx.android.synthetic.main.confirm_dialog_error.view.*
import java.util.*

class   Login : AppCompatActivity(){



    lateinit var locationManager: LocationManager
    lateinit var notificationManager: NotificationManager
    lateinit var viewDialog:ViewDialog
    private lateinit var auth: FirebaseAuth

    private var email: EditText? = null
    private var password: EditText? = null

    private var hasGps = false
    private var hasNetwork = false
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    var latitud= 0.00
    var longitud = 0.00
    var deviceID = ""
    var dbHandler: DatabaseHandler? = null
    var  getEmployes= Employes_Document()

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        deviceID = Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID)

        dbHandler = DatabaseHandler(this)

        super.onCreate(savedInstanceState)

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setContentView(R.layout.activity_login)

            viewDialog = ViewDialog(this)
            auth = FirebaseAuth.getInstance()

            email = findViewById<EditText>(R.id.edtUser) as EditText
            password = findViewById<EditText>(R.id.edtPassword) as EditText


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try{
                getLocation()}
            catch (ex:Exception){
                println("error permisos para acceder ubicacion $ex")
            }
        } else {
            Toast.makeText(applicationContext,"No tiene permisos",Toast.LENGTH_LONG).show()
        }
        calendarDay()
        }


    fun btnSignUp(view: View){

        var intent= Intent(baseContext, SignUpActivity::class.java)
        startActivity(intent)

    }




    fun btnLogin(view: View){
        if(email!!.text.isEmpty())
        {
            val edtUserdInputLayout = this.findViewById<TextInputLayout>(R.id.input_layout_user)
            edtUserdInputLayout.error = "Usuario es requeirdo!"
            edtUserdInputLayout.isErrorEnabled = true
            /*edtUser.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    edtUserdInputLayout.isErrorEnabled = false
                }
            })*/
        }
        if(password!!.text.isEmpty())
        {
            val edtPassworddInputLayout = this.findViewById<TextInputLayout>(R.id.input_layout_password)
            edtPassworddInputLayout.error = "Password es requeirdo!"
            edtPassworddInputLayout.isErrorEnabled = true
            /*edtPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    edtPassworddInputLayout.isErrorEnabled = false
                }
            })*/
        }
        else {
            showCustomLoadingDialog()
            getUserLicenceTask().execute()
        }
    }


    fun showCustomLoadingDialog() {
        viewDialog.showDialog()
        val handler = Handler()
        handler.postDelayed({
            viewDialog.hideDialog()
        }, 10000)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                            DataTracks.lat = latitud
                            DataTracks.long = longitud
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
                            latitud = locationNetwork!!.latitude
                            longitud = locationNetwork!!.longitude
                            DataTracks.lat = latitud
                            DataTracks.long = longitud
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
                    DataTracks.lat = latitud
                    DataTracks.long = longitud
                }else{
                    latitud = locationGps!!.latitude
                    longitud = locationGps!!.longitude
                    DataTracks.lat = latitud
                    DataTracks.long = longitud
                }
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    inner class getUserLicenceTask : AsyncTask<String, String, String>() {

        private var validacion =false
        override fun onPreExecute() {
            println("preExecutee" )
        }

        override fun doInBackground(vararg urls: String?): String {
            try {
                println("backgroun inicio" )
                    val db = FirebaseFirestore.getInstance()
                    db.collection("popusers")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    println("callDocument ${document.id} => ${document.data}")
                                    val callUser = document.toObject(Popusers::class.java)


                if(email!!.text.toString() == callUser.username && password!!.text.toString() == callUser.password) {
                    //callUser.employeeCorreo
                    //callUser.employeeNSS
                    showCustomLoadingDialog()
                    validacion=true
                    sessionStorage.popUserId = callUser.userid
                    sessionStorage.userName = callUser.username
                    println("Logintrue")
                    loginKubeet(validacion)
                    var intent= Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    println("errorLogin")
                    loginKubeet(validacion)
                }
                                }
                            }
                            .addOnFailureListener { exception ->
                                println("ErrorgetLog")
                            }
            } catch (ex: Exception) {
                println("errorAccesLogin $ex ")
            }
            return " "
        }
        override fun onProgressUpdate(vararg values: String?) {
            try {
            } catch (ex: Exception) {
            }
        }
        override fun onPostExecute(result: String?) {
            if(!validacion)
            {
                viewDialog.hideDialog()
                showCustomLoadingDialog()
            }
            println("onPostExecute $result" )
            }
    }

    private fun alertNotUSer() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.confirm_dialog_error, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
        val  mAlertDialog = mBuilder.show()
        mDialogView.btnAceptarDialog.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    fun loginKubeet(responseType: Boolean) {
        if(responseType)
        {
            showCustomLoadingDialog()
            var intent= Intent(this, MainActivity::class.java)
            intent.putExtra("userName", getEmployes.employeeCorreo)

            //Toast.makeText(this, "Bienvenido " + getEmployes.employeeCorreo + " "+ getEmployes.employeeNSS , Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
        else{
            alertNotUSer()
        }
    }

    fun calendarDay() {
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        when (day) {
            Calendar.MONDAY -> {}
            Calendar.TUESDAY -> {}
            Calendar.WEDNESDAY -> {}
            Calendar.THURSDAY -> {}
            Calendar.FRIDAY -> {}
            Calendar.SATURDAY -> {}
            Calendar.SUNDAY -> {}
        }
        println("printDay $day")

    }
}



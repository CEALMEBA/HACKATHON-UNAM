package com.kubeet.pop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.R.id.add
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.hussein.startup.R
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import kotlinx.android.synthetic.main.confirm_dialog_error.view.*
import kotlinx.android.synthetic.main.signup.*
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val States = arrayOf(
            "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
            "Chihuahua", "Coahuila de Zaragoza", "Colima", "Durango", "Estado de México",
            "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán de Ocampo", "Morelos",
            "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
            "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz de Ignacio de la Llave",
            "Yucatán", "Zacatecas"
    )


    val Moths = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
            "Ocutubre", "Noviembre", "Diciembre"
    )

    val Days = arrayOf(
            " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", " 10 ", " 11 ", " 12 ", " 13 ",
            " 14 ", " 15 ", " 16 ", " 17 ", " 18 ", " 19 ", " 20 ", " 21 ", " 22 ", " 23 ", " 24 ", " 25 ", " 26 ",
            " 27 ", " 28 ", " 29 ", " 30 ", " 31 "
    )

    val Preferencias = arrayOf(
            "Bares", "Bazares", "Cafes", "centros comerciales", "Charcuteria", "Discotecas", "Ferreterias",
            "Hoteles", "Joyerias", "Mercados", "Plazas de abastos", "Reposteria", "Restaurantes", "Antro",
            "Boutique", "Cibercafé", "Heladeria", "Jugueteria", "Licoreria", "Merceria", "Salon de belleza",
            "Sex shop"
    )


    lateinit var Nombre: EditText
    lateinit var Apellido: EditText
    lateinit var SegundoApe: EditText
    lateinit var Correo: EditText
    lateinit var Contraseña: EditText
    lateinit var Direccion: EditText
    lateinit var Ciudad: EditText
    lateinit var CodigoPostal: EditText
    lateinit var Estado: Spinner
    lateinit var Mes: Spinner
    lateinit var Dia: Spinner
    lateinit var Preferences: Spinner

    lateinit var btnRegister: Button



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        //setSupportActionBar(toolbar)


        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setHomeAsUpIndicator(R.drawable.return_page)

        Nombre = findViewById(R.id.txtNombre) as EditText
        Apellido = findViewById(R.id.txtApellido) as EditText
        SegundoApe = findViewById(R.id.txtCorreo) as EditText
        Correo = findViewById(R.id.txtCorreo) as EditText
        Contraseña = findViewById(R.id.txtContraseña) as EditText
        Direccion = findViewById(R.id.txtDireccion) as EditText
        Ciudad = findViewById(R.id.txtCiudad) as EditText
        CodigoPostal = findViewById(R.id.txtCodigoPostal) as EditText
        btnRegister = findViewById(R.id.btnRegister) as Button
        Estado = findViewById(R.id.mySpinner) as Spinner
        Mes = findViewById(R.id.mySpinnerMes) as Spinner
        Dia = findViewById(R.id.mySpinnerDia) as Spinner
        Preferences = findViewById(R.id.mySpinnerPreferencias) as Spinner


        var est = ArrayAdapter(this, android.R.layout.simple_spinner_item, States)
        est.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        with(mySpinner)
        {
            adapter = est
            setSelection(0, false)
            onItemSelectedListener = this@SignUpActivity
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }

        var month = ArrayAdapter(this, android.R.layout.simple_spinner_item, Moths)
        est.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        with(mySpinnerMes)
        {
            adapter = month
            setSelection(0, false)
            onItemSelectedListener = this@SignUpActivity
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }

        var day = ArrayAdapter(this, android.R.layout.simple_spinner_item, Days)
        est.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        with(mySpinnerDia)
        {
            adapter = day
            setSelection(0, false)
            onItemSelectedListener = this@SignUpActivity
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }


        var preferences = ArrayAdapter(this, android.R.layout.simple_spinner_item, Preferencias)
        est.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        with(mySpinnerPreferencias)
        {
            adapter = preferences
            setSelection(0, false)
            onItemSelectedListener = this@SignUpActivity
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }


        btnRegister.setOnClickListener{

            confirmRegister()
        }


    }




    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (view?.id) {
            1 -> println(message = "Spinner 2 Position:${position} and language: ${States[position]}")
                //showToast(message = "Spinner 2 Position:${position} and language: ${States[position]}")
            else -> {
                //showToast(message = "Spinner 1 Position:${position} and language: ${States[position]}")
                println(message = "Spinner 1 Position:${position} and language: ${States[position]}")
            }
        }
    }

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }


    fun addRegister(){

        try {
            val db = FirebaseFirestore.getInstance()
            var ref = db.collection("popusers").document()
            var myId = ref.getId()
            sessionStorage.docKey = myId
            val register = hashMapOf(

                    "mount" to 1,
                    "password" to Contraseña.text.toString(),
                    "status" to 5,
                    "username" to Nombre.text.toString(),
                    "email" to Correo.text.toString(),
                    "surname" to Apellido.text.toString(),
                    "secondSurname" to SegundoApe.text.toString(),
                    "address" to Direccion.text.toString(),
                    "city" to Ciudad.text.toString(),
                    "postalcode" to CodigoPostal.text.toString(),
                    "state" to Estado.selectedItem.toString(),
                    "moth" to Mes.selectedItem.toString(),
                    "day" to Dia.selectedItem.toString(),
                    "preferences" to Preferences.selectedItem.toString(),
                    "userid" to myId


            )
            db.collection("popusers").document(myId).set(register)
                    .addOnSuccessListener {
                        println("add Information")
                    }
                    .addOnFailureListener { e ->
                        println("Error writing document $e")
                    }
        }catch (ex: Exception){
            println("Error Register")
        }
    }



    fun confirmRegister(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.confirm_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
        val  mAlertDialog = mBuilder.show()
        mDialogView.btnAceptar.setOnClickListener {
            addRegister()
            var intent= Intent(baseContext, Login::class.java)
            startActivity(intent)
        }

        mDialogView.btnCancelar.setOnClickListener{
            mAlertDialog.dismiss()
        }
    }

    override fun onSupportNavigateUp():Boolean {
        finish()
        return true
    }

}

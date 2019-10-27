package com.kubeet.smarturban

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.hussein.startup.R
import kotlinx.android.synthetic.main.confirm_dialog.view.*

class MainActivity : AppCompatActivity() {


    lateinit var cd: ConnectionDetector

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        cd = ConnectionDetector()
        cd.isConnectingToInternet(this@MainActivity)


        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.id_maps -> {
                    val fragment = MapsMainFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                R.id.id_ticket -> {
                    val fragment = TicketFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                R.id.id_horario -> {
                    val fragment = SchedulesCarsFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                R.id.id_configuration -> {

                    /*var intent= Intent(baseContext, SchedulesCars::class.java)
                    startActivity(intent)*/

                    val fragment = ConfigurationFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                else -> false
            }
        }

        bottom_navigation_view.selectedItemId = R.id.id_maps

        verifiConnection()

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onKeyDown(keyCode:Int, event: KeyEvent):Boolean {
        super.onKeyDown(keyCode, event)
        var session = false

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.confirm_close_session, null)
            val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.btnAceptar.setOnClickListener {
                //finish()

                var intent= Intent(this, Login::class.java)
                startActivity(intent)

                session = true
            }
            mDialogView.btnCancelar.setOnClickListener {
                session = false
                mAlertDialog.dismiss()
            }
        }
        return session
    }


    fun verifiConnection(){

        if (cd.isConnectingToInternet(this@MainActivity)) {
            Toast.makeText(applicationContext,
                    "conexion internet true", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext,
                    "conexion internet false", Toast.LENGTH_LONG).show()
        }
    }


}

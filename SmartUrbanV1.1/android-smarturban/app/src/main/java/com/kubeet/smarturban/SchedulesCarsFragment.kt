package com.kubeet.smarturban

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hussein.startup.R
import com.kubeet.models.*
import kotlinx.android.synthetic.main.fragment_schedulescars.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_schedulescars.*


class SchedulesCarsFragment : Fragment(){

    companion object {
        fun newInstance(): SchedulesCarsFragment = SchedulesCarsFragment()
    }

    lateinit var  mlistView : ListView

    var  ListSchedulesCars = java.util.ArrayList<SchedulesCars_Document>()
    var button_background : Int = 1
    var ListRouteDetail = ArrayList<RouteDetails_Document>()

    var counter: Int = 0
    var i = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragmet_schedules,container,false)

        mlistView = view.findViewById<ListView>(R.id.listActiv) as ListView

        callSchedulesCars()
        calendarDay()

        return view
    }

    fun callSchedulesCars() {

        var everyDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        println("this day $everyDate")

        val db = FirebaseFirestore.getInstance()
        db.collection("schedulecars")
                //.orderBy("travelDate", Query.Direction.ASCENDING)
                .whereEqualTo("date", everyDate)
                .whereEqualTo("identifier", "Pi1bFefhtFVtbEpDKRYx")
                .whereEqualTo("unitId", sessionStorage.carId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getSchedulesCars ${document.id} => ${document.data}")

                        val callSchedulesCars = document.toObject(SchedulesCars_Document::class.java)

                        ListSchedulesCars.add(
                                SchedulesCars_Document(

                                        key= callSchedulesCars.key,
                                        boardid = callSchedulesCars.boardid,
                                        checkin = callSchedulesCars.checkin,
                                        checkout = callSchedulesCars.checkout,
                                        comments = callSchedulesCars.comments,
                                        date = callSchedulesCars.date,
                                        dato2 = callSchedulesCars.dato2,
                                        driver = callSchedulesCars.driver,
                                        duration = callSchedulesCars.duration,
                                        identifier = callSchedulesCars.identifier,
                                        shortId = callSchedulesCars.shortId,
                                        source = callSchedulesCars.source,
                                        status = callSchedulesCars.status,
                                        target = callSchedulesCars.target,
                                        unitId = callSchedulesCars.unitId,
                                        userid = callSchedulesCars.userid


                                )

                        )
                        callAdpater()
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getShedulesCars $exception")
                }
    }

    private fun callAdpater() {
        var myNotesAdapter= MyNotesAdpater(context!!, ListSchedulesCars)
        mlistView.adapter=myNotesAdapter
        println("printAdapter ${myNotesAdapter.count}")
    }

    private val buttonState = 1
    private val state = 0

    inner class  MyNotesAdpater:BaseAdapter{
        var listNotesAdpater= java.util.ArrayList<SchedulesCars_Document>()
        var context:Context?=null
        constructor(context:Context, listNotesAdpater: java.util.ArrayList<SchedulesCars_Document>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }
        @SuppressLint("ResourceAsColor")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var myView=layoutInflater.inflate(R.layout.fragment_schedulescars,null)
            var myNote=listNotesAdpater[p0]

            //=======================================
            val sdf = SimpleDateFormat("EEEE")
            val d = Date()
            val dayOfTheWeek = String//sdf.format(d)

/*
            val calendar = Calendar.getInstance()
            var day = calendar.get(Calendar.DAY_OF_WEEK)

            if (myNote.boardId === 0){
                day = Calendar.SUNDAY
            }

            if (myNote.boardId === 1){
                day = Calendar.MONDAY
            }

            if (myNote.boardId ===2) {
                day = Calendar.TUESDAY
            }

            if (myNote.boardId ===3){
                day = Calendar.WEDNESDAY
            }

            if (myNote.boardId ===4){
                day = Calendar.THURSDAY
            }

            if (myNote.boardId ===5){
                day = Calendar.FRIDAY
            }

            if (myNote.boardId ===6){
                day = Calendar.SATURDAY
            }*/

            //println("format day $day")


            //=========================================

            myView.txtRoute.text = myNote.target
            myView.txtUnit.text = myNote.unitId
            myView.txtTravelDate.text = myNote.date+" "+myNote.checkin+" - "+myNote.checkout
            myView.txtDriver.text = myNote.driver

            println("print myroute ${myNote.key}")


            myView.btnStartFinish.setOnClickListener {
                try {
                    sessionStorage.routeName = myNote.source+""+myNote.target
                    sessionStorage.routeId = myNote.identifier

                    println("print session route  ${sessionStorage.routeId}")

                    counter++

                    //myView.btnStartFinish.setText("Finish")
                    //myView.btnStartFinish.setBackgroundColor(Color.RED)
                    //Toast.makeText(context, "print route key ${sessionStorage.routeId}", Toast.LENGTH_SHORT).show()



                    if (i == 0)
                    {
                        myView.btnStartFinish.setText("Finalizar Viaje")
                        myView.btnStartFinish.setBackgroundColor(Color.RED)

                        i++
                    }
                    else if (i == 1)
                    {
                        myView.btnStartFinish.setText("Viaje Finalizado")
                        myView.btnStartFinish.setBackgroundColor(Color.GRAY)
                        btnStartFinish.isEnabled = false
                        i = 0
                    }

                    /*
                    if (state == 0) {
                        Toast.makeText(context, "touch start", Toast.LENGTH_SHORT).show()
                    }
                    else if (state == 1) {
                        Toast.makeText(context, "touch finish", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "touch error", Toast.LENGTH_SHORT).show()
                    }
*/


                }catch (ex:Exception){
                    println("errorAction $ex")
                }
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

    fun startTravel(){
        var everyDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())
        val db = FirebaseFirestore.getInstance()
        val travels = hashMapOf(
                "carid" to sessionStorage.carId,
                "date" to everyDate,
                "driverid" to sessionStorage.key,
                "lat" to  sessionStorage.latitud,
                "lng" to  sessionStorage.longitud,
                "routename" to sessionStorage.routeName,
                //"routeid" to sessionStorage.routeId,
                "userid" to sessionStorage.userId
        )
        db.collection("travels").add(travels)
                .addOnSuccessListener {
                    println("add Information") }
                .addOnFailureListener { e ->
                    println("Error writing document $e") }
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
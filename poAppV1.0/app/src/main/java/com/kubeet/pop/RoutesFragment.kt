package com.kubeet.pop


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.hussein.startup.R
import com.kubeet.models.Routes_Document
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.fragment_routes.view.*
import java.sql.DriverManager


class RoutesFragment : Fragment() {

    companion object {
        fun newInstance(): RoutesFragment = RoutesFragment()
    }

    lateinit var  mlistView : ListView
    var  ListRoutes = ArrayList<Routes_Document>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        container!!.removeAllViews()
        val view = inflater!!.inflate(R.layout.fragment_list_routes,container,false)

        mlistView = view.findViewById(R.id.listRoutes) as ListView

        callWallets()
        return view
    }

    fun callWallets(){

        val db = FirebaseFirestore.getInstance()
        db.collection("routes")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getRoutes ${document.id} => ${document.data}")

                        val callRoutes = document.toObject(Routes_Document::class.java)

                        println("printChild ${callRoutes.nombrekey}")

                        ListRoutes.add(
                                Routes_Document(
                                        color = callRoutes.color,
                                        duration = callRoutes.duration,
                                        img = callRoutes.img,
                                        nombrekey = callRoutes.nombrekey,
                                        source = callRoutes.source,
                                        status = callRoutes.status,
                                        target = callRoutes.target,
                                        userid = callRoutes.userid
                                )
                        )

                        println("calluserid ${callRoutes.userid}")
                        callAdpater()
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getDocuments")
                }
    }


    private fun callAdpater() {
        var myNotesAdapter= MyNotesAdpater(context!!, ListRoutes)
        mlistView.adapter=myNotesAdapter
        println("printAdapter ${myNotesAdapter.count}")
    }

    inner class  MyNotesAdpater: BaseAdapter {
        var listNotesAdpater=ArrayList<Routes_Document>()
        var context:Context?=null
        constructor(context:Context, listNotesAdpater:ArrayList<Routes_Document>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {


            var myView=layoutInflater.inflate(R.layout.fragment_routes,null)
            var myNote=listNotesAdpater[p0]

            myView.txtRuta.text = myNote.nombrekey

            println("printuserid ${myNote.userid}")

            myView!!.setOnClickListener {

                sessionStorage.userTravel = myNote.userid

                openRoutes()

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


    fun openRoutes() {
           try {
/*                var intent= Intent(baseContext, ReciboActivity::class.java)
                startActivity(intent)


                println("printMount ${sessionStorage.mountWallet}")*/

            var registroFragment = MapsMainFragment()
            fragmentManager!!.beginTransaction()
                    .replace(R.id.id_routes, registroFragment)
                    //.addToBackStack(null)
                    .commit()
            }catch (exe:Exception){
                println("error cambio de fragment $exe")

            }

        }

}
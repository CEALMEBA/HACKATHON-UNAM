package com.kubeet.pop


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.FirebaseFirestore
import com.hussein.startup.R
import com.kubeet.models.Wallets_Document
import com.kubeet.models.sessionStorage
import java.sql.DriverManager.println
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    lateinit var mount: TextView
    lateinit var welcome: TextView
    lateinit var detail: Button
    lateinit var explorar: Button



    var ListWallets = ArrayList<Wallets_Document>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        container!!.removeAllViews()
        val view = inflater!!.inflate(R.layout.fragment_home,container,false)

        mount = view.findViewById(R.id.txtMount) as TextView
        welcome = view.findViewById(R.id.txtWelcome) as TextView
        detail = view.findViewById(R.id.btnDetail) as Button
        explorar = view.findViewById(R.id.btnExplorar) as Button


        detail.setOnClickListener{
            openMonedero()
        }

        explorar.setOnClickListener(){
            openMaps()
        }



        callWS()

        return view
    }

    fun callWS(){

        val db = FirebaseFirestore.getInstance()
        db.collection("wallets")
                .whereEqualTo("popuserid", sessionStorage.popUserId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getDocum ${document.id} => ${document.data}")

                        val callWallets = document.toObject(Wallets_Document::class.java)


                        val builder = LatLngBounds.Builder()
                        ListWallets.add(
                                Wallets_Document(
                                        childcode = callWallets.childcode,
                                        datemov = callWallets.datemov,
                                        mount = callWallets.mount,
                                        partnerid = callWallets.partnerid,
                                        tipomov = callWallets.tipomov,
                                        userid = callWallets.userid
                                )
                        )

                        /*if (callWallets.mount >= 1){

                            callWallets.mount += ListWallets.first().mount

                            mount.text = "$ "+ callWallets.mount
                        }*/



                        ListWallets.forEach{

                             /*for (i in 0..it.mount)
                                // sum = sum+i;
                                it.mount += i


                                mount.text = "$ "+it.mount*/


                            val totalPriceInList1: Int = ListWallets.map { it.mount }.sum()
                            println("printSum1" + totalPriceInList1)

                            /*val totalPriceInList2: Int = ListWallets.sumBy { it.mount }
                            println("printSum2" + totalPriceInList2)*/

                            mount.text = "$ " + totalPriceInList1.toString()
                        }




                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getDocuments")
                }


        welcome.text = sessionStorage.userName

    }



    fun openMonedero() {
        try {
            val myIntent = Intent(activity, MonederoActivity::class.java)
            activity!!.startActivity(myIntent)
/*
            var registroFragment = SoporteViajePruebasFragment()
            fragmentManager!!.beginTransaction()
                    .replace(R.id.id_fragment_sopviaje, registroFragment)
                    //.addToBackStack(null)
                    .commit()*/
        }catch (exe:Exception){
            println("error cambio de fragment $exe")

        }

    }


    fun openMaps() {
        try {
            val fragment = MapsMainFragment.newInstance()
            openFragment(fragment)
        }catch (exe:Exception){
            println("error cambio de fragment $exe")

        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
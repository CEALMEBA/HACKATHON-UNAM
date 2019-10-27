package com.kubeet.smarturban


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.hussein.startup.R


class ConfigurationFragment : Fragment() {

    companion object {
        fun newInstance(): ConfigurationFragment = ConfigurationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        container!!.removeAllViews()
        val view = inflater!!.inflate(R.layout.fragmet_configuration,container,false)




        return view
    }

}
package com.kubeet.smarturban

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.hussein.startup.R

class ViewDialog(activity:Activity) {
    internal var activity:Activity
    internal lateinit var dialog:Dialog
    lateinit var  animationDrawable: AnimationDrawable
    lateinit var  mProgressBar: ImageView

    init{
        this.activity = activity
    }
    fun showDialog() {
        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_loading)
        mProgressBar = dialog.findViewById(R.id.custom_loading_imageView)

        mProgressBar.setBackgroundResource(R.drawable.loading_efl)
        animationDrawable = mProgressBar.background as AnimationDrawable
        mProgressBar.visibility = View.VISIBLE
        animationDrawable.start()


        //val imageViewTarget = GlideDrawableImageViewTarget(gifImageView)
        //Glide.with(activity)
        //        .load(R.drawable.loading)
        //        .placeholder(R.drawable.loading)
        //        .centerCrop()
        //        .crossFade()
        //        .into(imageViewTarget)
        dialog.show()
    }
    fun hideDialog() {
        dialog.dismiss()
    }
}

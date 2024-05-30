package com.alcorp.githubuser.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import com.alcorp.githubuser.R

fun checkNetwork(context: Context?): Boolean{
    val cm: ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            cm.getNetworkCapabilities(cm.activeNetwork)
        } else {
            cm.activeNetworkInfo
        }
    return network != null
}

class LoadingDialog(private val context: Context) {
    private lateinit var dialog: Dialog

    fun showDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}
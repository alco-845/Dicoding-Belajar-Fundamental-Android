package com.alcorp.broadcastreceiverapp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val ACTION_DOWNLOAD_STATUS = "download_status"
        private const val SMS_REQUEST_CODE = 101
    }

    private lateinit var downloadReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_permission.setOnClickListener(this)
        btn_download.setOnClickListener(this)

        downloadReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context, intent: Intent?) {
                Log.d(DownloadService.TAG, "Download finished")
                Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show()
            }
        }

        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS)
        registerReceiver(downloadReceiver, downloadIntentFilter)
    }

    override fun onClick(v: View) {
        when {
            v.id == R.id.btn_permission -> PermissionManager.check(this, Manifest.permission.RECEIVE_SMS, SMS_REQUEST_CODE)
            v.id == R.id.btn_download-> {
                val downloadServiceIntent = Intent(this, DownloadService::class.java)
                startService(downloadServiceIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == SMS_REQUEST_CODE){
            when{
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> Toast.makeText(this, "Sms receiver permission accepted", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Sms receiver permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

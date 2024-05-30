package com.alcorp.broadcastreceiverapp

import android.app.IntentService
import android.content.Intent
import android.util.Log

class DownloadService : IntentService("DownloadService") {

    companion object{
        val TAG = DownloadService::class.java.simpleName
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "Download Service dijalankan")
        if (intent != null){
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException){
                e.printStackTrace()
            }

            val notifyFinishedIntent = Intent(MainActivity.ACTION_DOWNLOAD_STATUS)
            sendBroadcast(notifyFinishedIntent)
        }
    }

}

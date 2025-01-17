package com.alcorp.mediaplayerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.lang.ref.WeakReference

class MediaService : Service(), MediaPlayerCallback {

    private val TAG = MediaService::class.java.simpleName
    private var isReady: Boolean = false
    private var mMediaPlayer: MediaPlayer? = null

    companion object {
        const val ACTION_CREATE = "com.alcorp.mediaplayerapp.mediaservice.create"
        const val ACTION_DESTROY = "com.alcorp.mediaplayerapp.mediaservice.destroy"
        const val PLAY = 0
        const val STOP = 1
    }

    /**
    Ketika kelas service terbentuk, secara otomatis akan memanggil method init()
    */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    /**
    Method yang akan dipanggil ketika service dimulai
    */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null){
            when(action){
                ACTION_CREATE -> if (mMediaPlayer == null){
                    init()
                }

                ACTION_DESTROY -> if (mMediaPlayer?.isPlaying as Boolean){
                    stopSelf()
                }

                else -> {
                    init()
                }
            }
        }

        Log.d(TAG, "onStartCommand: ")
        return flags
    }


    /**
    Method yang akan dipanggil ketika service di ikatkan ke activity
    */
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind: ")
        return mMessenger.binder
    }

    /**
    Method yang akan dipanggil ketika service di lepas dari activity
    */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    /**
    Method yang akan dipanggil service terlepas dari memory
    */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
//        mMediaPlayer?.release()
    }

    /**
    Method ini berfungsi untuk menginisialisasi mediaplayer
    */
    private fun init() {
        mMediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val attribute = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            mMediaPlayer?.setAudioAttributes(attribute)
        } else {
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        val afd = applicationContext.resources.openRawResourceFd(R.raw.jalastram__techno_drum_loop_003)
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException){
            e.printStackTrace()
        }

        /**
         * Called when MediaPlayer is ready
         */
        mMediaPlayer?.setOnPreparedListener(){
            isReady = true
            mMediaPlayer?.start()
            showNotif()
        }

        mMediaPlayer?.setOnErrorListener{ mp, what, extra ->
            /**
             * Called when MediaPlayer is error
             */
            false
        }
    }

    /**
     * Callback ketika button play di klik
     */
    override fun onPlay() {
        if (!isReady){
            mMediaPlayer?.prepareAsync()
        } else {
            if (mMediaPlayer?.isPlaying as Boolean){
                mMediaPlayer?.pause()
            } else {
                mMediaPlayer?.start()
                showNotif()
            }
        }
    }

    /**
     * Callback ketika button stop di klik
     */
    override fun onStop() {
        if (mMediaPlayer?.isPlaying as Boolean || isReady){
            mMediaPlayer?.stop()
            isReady = false
            stopNotif()
        }
    }

    /**
     * Digunakan ketika media service berjalan, maka akan muncul notif
     */
    private fun showNotif() {
        val CHANNEL_DEFAULT_IMPORTANCE = "Channel_Test"
        val ONGOING_NOTIFICATION_ID = 1

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle("TEST 1")
            .setContentText("TEST 2")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setTicker("TEST 3")
            .build()

        createChannel(CHANNEL_DEFAULT_IMPORTANCE)

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    private fun createChannel(CHANNEL_ID: String) {
        val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, "Battery", NotificationManager.IMPORTANCE_DEFAULT)
            channel.setShowBadge(false)
            channel.setSound(null, null)
            mNotificationManager.createNotificationChannel(channel)
        }
    }

    private fun stopNotif() {
        stopForeground(false)
    }

    /**
     * Method incomingHandler sebagai handler untuk aksi dari onklik button di MainActivity
     */
    private val mMessenger = Messenger(IncomingHandler(this))

    internal class IncomingHandler(playerCallback: MediaPlayerCallback) : Handler(){
        private val mediaPlayerCallbackWeakReference: WeakReference<MediaPlayerCallback> = WeakReference(playerCallback)

        override fun handleMessage(msg: Message) {
            when(msg.what){
                PLAY -> mediaPlayerCallbackWeakReference.get()?.onPlay()

                STOP -> mediaPlayerCallbackWeakReference.get()?.onStop()

                else -> super.handleMessage(msg)
            }
        }
    }
}
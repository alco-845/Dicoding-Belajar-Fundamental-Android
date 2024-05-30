package com.alcorp.soundpoolapp

import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = SoundPool.Builder()
                .setMaxStreams(10)
                .build()

        /**
        Tambahkan listener ke soundpool jika proses load sudah selesai
         */
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0){
                spLoaded = true
            } else {
                Toast.makeText(this@MainActivity, "Gagal Load", Toast.LENGTH_SHORT).show()
            }
        }

        /**
        Load raw file ke soundpool, jika selesai maka id nya dimasukkan ke variable soundId
         */
        soundId = sp.load(this, R.raw.jalastram__techno_drum_loop_003, 1) // in 2nd param u have to pass your desire ringtone

        btn_soundpool.setOnClickListener {
            if (spLoaded){
                sp.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }
    }
}
package com.alcorp.widgetapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val JOB_ID = 100
        private const val SCHEDULE_OF_PERIOD = 8600L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener(this)
        btn_stop.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_start -> startJob()
            R.id.btn_stop -> cancelJob()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startJob() {
        val mServiceComponent = ComponentName(this, UpdateWidgetService::class.java)

        val builder = JobInfo.Builder(JOB_ID, mServiceComponent)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            builder.setPeriodic(9000) // 15 menit
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD) // 3 menit
        }
        val jobSheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobSheduler.schedule(builder.build())

        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cancelJob() {
        val tm = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        tm.cancel(JOB_ID)
        Toast.makeText(this, "Job Service canceled", Toast.LENGTH_SHORT).show()
    }
}
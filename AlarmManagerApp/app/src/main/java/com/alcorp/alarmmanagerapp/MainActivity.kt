package com.alcorp.alarmmanagerapp

import androidx.appcompat.app.AppCompatActivity
import com.alcorp.alarmmanagerapp.utils.DatePickerFragment
import com.alcorp.alarmmanagerapp.utils.TimePickerFragment
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // One time onclick
        btn_once_date.setOnClickListener(this)
        btn_once_time.setOnClickListener(this)
        btn_set_once_alarm.setOnClickListener(this)

        // Repeat onclick
        btn_repeating_time.setOnClickListener(this)
        btn_set_repeating_alarm.setOnClickListener(this)
        btn_cancel_repeating_alarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_once_date -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }

            R.id.btn_once_time -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }

            R.id.btn_set_once_alarm -> {
                val onceDate = tv_once_date.text.toString()
                val onceTime = tv_once_time.text.toString()
                val onceMessage = edt_once_message.text.toString()

                alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME, onceDate, onceTime, onceMessage)
            }

            R.id.btn_repeating_time -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }

            R.id.btn_set_repeating_alarm -> {
                val repeatTime = tv_repeating_time.text.toString()
                val repeatMessage = edt_repeating_message.text.toString()
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage)
            }

            R.id.btn_cancel_repeating_alarm -> alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        tv_once_date.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_ONCE_TAG -> tv_once_time.text = dateFormat.format(calendar.time)
            TIME_PICKER_REPEAT_TAG -> tv_repeating_time.text = dateFormat.format(calendar.time)
        }
    }
}
package com.pravin.androidschedulework.AlarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.*

object AlarmManagerUtil {
    private val TAG = "**AlarmUtil"
    private val TIME_PICKER_TAG = "TIMEPICKER"
    private lateinit var calendar:Calendar
    private lateinit var alarmManagerCompat: AlarmManager
    private lateinit var pendingIntent:PendingIntent

    fun setAlarm(context: Context,sfm:FragmentManager){
        calendar = Calendar.getInstance()
        val timePicker:MaterialTimePicker = MaterialTimePicker.Builder()
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText("Schedule").build()

        timePicker.addOnPositiveButtonClickListener{
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            Log.e(TAG, "Calendar set To  $calendar")
            scheduleWork(context)
        }
        timePicker.show(sfm, TIME_PICKER_TAG)
    }

    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE //this is needed in Android 12
    } else {
        PendingIntent.FLAG_CANCEL_CURRENT
    }

    private fun scheduleWork(context: Context){
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra(AlarmReceiver.title, "Alarm")
        alarmIntent.putExtra(AlarmReceiver.desc, "Whatever the mind of man can conceive and believe, it can achieve")

        alarmManagerCompat = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        pendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent, flag)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(alarmManagerCompat.canScheduleExactAlarms()){
                //set setExact  --> added     <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
                alarmManagerCompat.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } else {
            //set setExact  --> added     <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
            alarmManagerCompat.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }

        Toast.makeText(context, "Work Scheduled ", Toast.LENGTH_LONG).show()
        Log.e(TAG, "Alarm Scheduled at $calendar")
    }

    fun cancelWork(){
        if (this::alarmManagerCompat.isInitialized && this::pendingIntent.isInitialized){
            alarmManagerCompat.cancel(pendingIntent)
            Log.e(TAG, "Alarm Canceled ")
        }
    }
}
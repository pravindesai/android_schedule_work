package com.pravin.androidschedulework.AlarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.pravin.androidschedulework.NotificationUtil

//Add in android manifest
class AlarmReceiver: BroadcastReceiver() {
    companion object{
        val title = "ALARM"
        val desc = "DESC"
    }
    override fun onReceive(context: Context, intent: Intent?) {

        val Title:String = intent?.getStringExtra(title)?:""
        val Desc:String = intent?.getStringExtra(desc)?:""
        NotificationUtil.Builder(context, Title, Desc).sendNotification()
        Log.e("**AlarmReveiver", "onReceive: " )

        val alarmService = Intent(context, AlarmService::class.java)
        alarmService.putExtra(AlarmService.MSG, "Alarm")
        ContextCompat.startForegroundService(context, alarmService)

    }
}
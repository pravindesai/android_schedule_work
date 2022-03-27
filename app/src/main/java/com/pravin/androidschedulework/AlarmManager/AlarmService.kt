package com.pravin.androidschedulework.AlarmManager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import javax.security.auth.login.LoginException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class AlarmService : Service() {
    companion object{
        val TAG = "**AlarmService"
        val MSG = "Message"
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val message:String = intent?.getStringExtra(MSG)?:""

        for(i in 1..5){
            Toast.makeText(this,"$message $i", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onStartCommand: $message $i")
            Thread.sleep(1000)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
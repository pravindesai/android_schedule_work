package com.pravin.androidschedulework

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pravin.androidschedulework.AlarmManager.AlarmReceiver

object NotificationUtil {
    private val CHANNELID = "WORKCHANNEL"
    private val CHANNEL_DESCRIPTION = "CAHNNEL FOR SCHEDULER"
    private val NOTIFICATION_ID = 123
    private lateinit var channel:NotificationChannel
    val attributeSet: AudioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNELID
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            channel = NotificationChannel(CHANNELID, name, importance)
            channel.description = descriptionText
        }
    }

     fun Builder(context: Context, title:String, message:String):Manager{
         val notificationManagerCompat:NotificationManagerCompat = NotificationManagerCompat.from(context)
         val soundUri: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://" + context.packageName + "/" + R.raw.britania)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             notificationManagerCompat.createNotificationChannel(channel)
             channel.setSound(soundUri, attributeSet)
         }

         val mainActivityIntent:Intent = Intent(context, MainActivity::class.java)
         mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

         val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
             PendingIntent.FLAG_MUTABLE //this is needed in Android 12
         } else {
             PendingIntent.FLAG_CANCEL_CURRENT
         }

         val mainPendingIntent:PendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, flag)

         val builder:NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNELID)
             .setSmallIcon(R.drawable.wiseman)
             .setContentTitle(title)
             .setContentText(message)
             .setAutoCancel(true)
             .setDefaults(NotificationCompat.DEFAULT_ALL)
             .setPriority(NotificationCompat.PRIORITY_HIGH)
             .setContentIntent(mainPendingIntent)
             .setSound(soundUri)

            val notification:Notification = builder.build()

            return Manager(notificationManagerCompat, notification)

     }

    class Manager(private val notificationManagerCompat:NotificationManagerCompat,private val notification: Notification){
        fun sendNotification() = notificationManagerCompat.notify(NOTIFICATION_ID, notification)

    }
}
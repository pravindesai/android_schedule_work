package com.pravin.androidschedulework.WorkManager

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.*
import com.pravin.androidschedulework.NotificationUtil.Builder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object WorkManagerUtil {
    private val TAG = "**WORK_MANAGER_UTIL"
    private lateinit var workManager:WorkManager
    private val ruleConstraints = Constraints.Builder()
        .setRequiresCharging(false) //checks whether device should be charging for the WorkRequest to run
        .setRequiredNetworkType(NetworkType.CONNECTED) //checks whether device should have Network Connection
        .setRequiresBatteryNotLow(true) // checks whether device battery should have a specific level to run the work request
        .setRequiresStorageNotLow(true) // checks whether device storage should have a specific level to run the work request
        .apply {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                setRequiresDeviceIdle(true) //checks whether device should be idle for the WorkRequest to run
            }
        }
        .build()

    private var data: Data.Builder = Data.Builder()


    fun scheduleWork(context: Context) {
        workManager = WorkManager.getInstance(context)
        val oneTimeworkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                                       .setInputData(data.apply { putInt("ID", 0) }.build())
                                       .build()
        val oneTimeworkRequest2: OneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                                       .setInputData(data.apply { putInt("ID", 1) }.build())
                                       .build()

        val periodicWorkRequest:PeriodicWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.HOURS)
                                        .setConstraints(ruleConstraints)
                                        .setInputData(data.apply { putInt("ID", 2) }.build())
                                        .build()

        workManager.beginWith(oneTimeworkRequest).then(oneTimeworkRequest2).enqueue()
//        workManager.enqueue(periodicWorkRequest)
            CoroutineScope(Dispatchers.IO).launch {
                val result = workManager.enqueue(oneTimeworkRequest).await()
                Log.e(TAG, "scheduleWork: "+result )
            }

    }
}

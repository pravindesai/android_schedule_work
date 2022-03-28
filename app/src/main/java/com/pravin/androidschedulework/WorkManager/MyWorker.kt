package com.pravin.androidschedulework.WorkManager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(val appContext: Context, val workerParams: WorkerParameters):Worker(appContext, workerParams) {
    val TAG = "**MyWorker"
    override fun doWork(): Result {
        val ID = inputData.getInt("ID", -1)

        Log.e(TAG, "doInBackground: Worker running " )
        for (i in 1..10){
            Log.e(TAG, "$ID Woking -> $i  on "+Thread.currentThread().name )
            Thread.sleep(1000)
        }
        return Result.success()
    }

    /*
      Result.success(): The work finished successfully.
      Result.failure(): The work failed.
      Result.retry(): The work failed and should be tried at another time according to its retry policy.
* */
}
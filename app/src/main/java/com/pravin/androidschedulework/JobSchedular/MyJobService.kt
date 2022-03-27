package com.pravin.androidschedulework.JobSchedular

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyJobService :JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        //Job should be performed in background otherwise ANR will occur
        doInBackground(params)
        return true
    }

    private fun doInBackground(params: JobParameters?) {
        val JOBID = params?.extras?.getInt(JOBIDKEY)?:"NULLKEY"

        Log.e(TAG, "doInBackground: Job running " )
            for (i in 1..10){
                Log.e(TAG, "JOB ID-> $JOBID  : "+i+"  "+Thread.currentThread().name )
                Thread.sleep(1000)
            }

    }

    override fun onStopJob(params: JobParameters?): Boolean {
        //returning true restarts job if terminated
        Log.e(TAG, "doInBackground: Job Stopped " )
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        JOB_ID = 0
        Log.e(TAG, "Service Destroyed " )
    }

    companion object {
        const val MIN:Int = 0
        const val MAX:Int = 5
        const val JOBIDKEY = "jobidkey"
        private const val TAG = "**MyJobSchedular"
        var JOB_ID = 0
    }
}
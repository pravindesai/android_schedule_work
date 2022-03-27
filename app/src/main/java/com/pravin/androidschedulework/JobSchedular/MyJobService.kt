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
        val JOBID = params?.extras?.getInt(JOBIDKEY)?:"NULLKEY"
        doInBackground(JOBID)
        return true
    }

    private fun doInBackground(JOBID: Any) {
        Log.e(TAG, "doInBackground: Job running " )
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..100){
                Log.e(TAG, "JOB ID-> $JOBID  : "+i )
                Thread.sleep(1000)
            }
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        //returning true restarts job if terminated
        Log.e(TAG, "doInBackground: Job Stopped " )
        return true
    }

    companion object {
        const val MIN:Int = 0
        const val MAX:Int = 5
        const val JOBIDKEY = "jobidkey"
        private const val TAG = "**MyJobSchedular"
    }
}
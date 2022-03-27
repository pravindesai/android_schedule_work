package com.pravin.androidschedulework.JobSchedular

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.persistableBundleOf
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.pravin.androidschedulework.AlarmManager.AlarmManagerUtil
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
object JobSchedularUtil {
    private val TAG = "**JOB_SCHEDULAR_UTIL"
    private val JOB_SCHEDULAR_PICKER_TAG = "JOB_SCHEDULAR"
    private lateinit var calendar:Calendar
    private var JOB_ID = 1

    private lateinit var jobSchedular:JobScheduler

    //Job schedular is not supposed to schedule task at exact time
    private fun setAlarm(context: Context, sfm: FragmentManager){
        calendar = Calendar.getInstance()
        val timePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText("Schedule").build()

        timePicker.addOnPositiveButtonClickListener{
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            Log.e(TAG, "Calendar set To  ${calendar}")
            scheduleWork(context)
        }
        timePicker.show(sfm, JOB_SCHEDULAR_PICKER_TAG)
    }

    fun scheduleWork(context: Context) {
        jobSchedular = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val componentName:ComponentName = ComponentName(context, MyJobService::class.java)

        if (jobSchedular.allPendingJobs.size < MyJobService.MAX){
            JOB_ID++
        }
        val bundle = persistableBundleOf(Pair(MyJobService.JOBIDKEY, JOB_ID))

        val jobInfo:JobInfo = JobInfo.Builder(JOB_ID, componentName)
            .setMinimumLatency(1000)
            .setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setExtras(bundle)
            .setPersisted(true).build()

        val isJobScheduled = jobSchedular.schedule(jobInfo)

        if (isJobScheduled == JobScheduler.RESULT_SUCCESS){
            Toast.makeText(context, "Job Scheduled Successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Job Scheduled Failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopJob(JobId:Int){
        if (this::jobSchedular.isInitialized){
            jobSchedular.cancel(JobId)
        }
    }

}
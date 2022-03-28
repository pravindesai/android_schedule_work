package com.pravin.androidschedulework

import android.app.AlarmManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pravin.androidschedulework.AlarmManager.AlarmManagerUtil
import com.pravin.androidschedulework.JobSchedular.JobSchedularUtil
import com.pravin.androidschedulework.WorkManager.WorkManagerUtil
import com.pravin.androidschedulework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alarmManagerTv.setOnClickListener(this)
        binding.jobSchedularTv.setOnClickListener(this)
        binding.workManager.setOnClickListener(this)

        supportActionBar?.title = "Schedule Tasks"

    }

    override fun onClick(view: View?) {
        when(view){
            binding.alarmManagerTv->{
                AlarmManagerUtil.setAlarm(this, supportFragmentManager)
            }
            binding.jobSchedularTv->{
                /**
                * Note: Beginning with API 30 (Build.VERSION_CODES.R),
                *  JobScheduler will throttle runaway applications.
                *  Calling schedule(android.app.job.JobInfo) and other such methods
                * with very high frequency can have a high cost and so,
                *  to make sure the system doesn't get overwhelmed,
                *  JobScheduler will begin to throttle apps,
                *  regardless of target SDK version.
                *
                *and works only on and oabove lollipop API 21
                * * */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    JobSchedularUtil.scheduleWork(this)
                } else {
                    Toast.makeText(this, "JobSchedular only work above API 21 i.e. LOLLIPOP", Toast.LENGTH_SHORT).show()
                }

            }
            binding.workManager->{
                WorkManagerUtil.scheduleWork(this)
            }
        }
    }
}
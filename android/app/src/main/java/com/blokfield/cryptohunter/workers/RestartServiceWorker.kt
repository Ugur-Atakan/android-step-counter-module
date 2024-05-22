package com.blokfield.cryptohunter.workers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blokfield.cryptohunter.services.BackgroundService

class RestartServiceWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
            BackgroundService.PREFS_NAME, Context.MODE_PRIVATE)
        val shouldRestart = sharedPreferences.getBoolean(BackgroundService.KEY_SHOULD_RESTART, true)

        if (shouldRestart) {
            val serviceIntent = Intent(applicationContext, BackgroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(serviceIntent)
            } else {
                applicationContext.startService(serviceIntent)
            }
        }
        return Result.success()
    }
}

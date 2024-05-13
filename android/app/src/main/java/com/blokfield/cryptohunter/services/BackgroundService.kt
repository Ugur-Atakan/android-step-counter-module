package com.blokfield.cryptohunter.services
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.blokfield.cryptohunter.MainActivity
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.blokfield.cryptohunter.R
import com.blokfield.cryptohunter.workers.RestartServiceWorker
import java.util.concurrent.TimeUnit

class BackgroundService : Service() {
    companion object {
        const val CHANNEL_ID = "sc_background_service_channel"
        const val NOTIFICATION_ID = 1
    }
    private lateinit var notificationIntent: Intent

    private fun checkPermissions(): Boolean {
        val activityRecognitionResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
        val foregroundServiceResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE)
        return activityRecognitionResult == PackageManager.PERMISSION_GRANTED &&
        foregroundServiceResult == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate() {
        super.onCreate()
        notificationIntent = Intent(this, MainActivity::class.java)
        createNotificationChannel()
    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!checkPermissions()) {
            Toast.makeText(this, "Please access Permissions for Activity", Toast.LENGTH_LONG).show()
            stopSelf()
            return START_NOT_STICKY
        }
        if (intent == null) {
            return START_NOT_STICKY
        }
        if (intent.action == "ACTION_STOP_SERVICE") {
            stopSelf()
            return START_NOT_STICKY
        }

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(NOTIFICATION_ID, getNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
        } else {
            startForeground(NOTIFICATION_ID, getNotification())
        }
        return START_STICKY
    }

    override fun onDestroy() {
        enqueueRestartWork()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        enqueueRestartWork()
    }

    private fun enqueueRestartWork() {
        val restartWork = OneTimeWorkRequestBuilder<RestartServiceWorker>().setInitialDelay(0, TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance(applicationContext).enqueue(restartWork)
    }

    private fun getNotification(): Notification {
        val stopIntent = Intent(this, BackgroundService::class.java)
        stopIntent.action = "ACTION_STOP_SERVICE"
        val stopPendingIntent = PendingIntent.getService(
                this, 0, stopIntent,PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step Counter is running")
                .setContentText("Counting your steps")
                .setSound(null)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_IMMUTABLE))
                .addAction(android.R.drawable.ic_media_pause, "Stop", stopPendingIntent)
                .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Step Counter Service Channel"
            val channelDescription = "This channel is used by the step counter service"
            val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

package io.ebkit.app.server

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class RunningService : Service(), KoinComponent {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            val uid = intent.getIntExtra(Intent.EXTRA_UID, -1)
            if (uid < 0) return
            scope.launch {

            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("RunningService", "onCreate")
        runForeground()
        registerPackageReceiver()
    }

    override fun onDestroy() {
        unregisterReceiver(packageReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun registerPackageReceiver() {
        scope.launch {

        }

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
        filter.addDataScheme("package")
        registerReceiver(packageReceiver, filter)
    }

    private fun runForeground() {
        val manager = NotificationManagerCompat.from(this)
        val channelName = "running_service_channel"
        val channel: NotificationChannelCompat = NotificationChannelCompat.Builder(
            channelName, NotificationManagerCompat.IMPORTANCE_MAX
        ).setName("后台服务").setVibrationEnabled(false).setSound(null, null).setShowBadge(false)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                channel
            )
        }
        val notificationId = 1
        val notification = NotificationCompat.Builder(this, channel.id)
//            .setSmallIcon(R.drawable.round_hourglass_empty_black_24)
            .setContentTitle("EbKit正在运行")
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_DEFERRED)
            .setPriority(NotificationCompat.PRIORITY_MIN).setAutoCancel(false).setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                notificationId, notification
            )
        } else {
            startForeground(
                notificationId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING,
            )
        }
    }
}
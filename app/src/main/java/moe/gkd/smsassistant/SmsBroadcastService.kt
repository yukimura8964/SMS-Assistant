package moe.gkd.smsassistant

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import moe.gkd.smsassistant.event.SmsBroadcastServiceStatusEvent
import org.greenrobot.eventbus.EventBus


class SmsBroadcastService : Service() {
    companion object {
        fun startService(context: Context) {
            val serviceIntent = Intent(context, SmsBroadcastService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }

        fun stopService(context: Context) {
            val serviceIntent = Intent(context, SmsBroadcastService::class.java)
            context.stopService(serviceIntent)
        }

        public var isRunning = false
    }

    private val TAG = this::class.java.simpleName
    private val CHANNEL_ID by lazy {
        packageName + ".permanent"
    }
    private val NOTIFICATION_ID = 0x8848
    private val smsBroadcastReceiver by lazy {
        SmsBroadcastReceiver()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate()")
        isRunning = true
        setForegroundService()
        registerSmsReceiver()
        EventBus.getDefault().post(SmsBroadcastServiceStatusEvent(true))
    }

    override fun onDestroy() {
        unregisterReceiver(smsBroadcastReceiver)
        isRunning = false
        EventBus.getDefault().post(SmsBroadcastServiceStatusEvent(false))
        super.onDestroy()
    }

    private fun registerSmsReceiver() {
        val filter = IntentFilter(SmsBroadcastReceiver.SMS_RECEIVED)
        filter.priority = Int.MAX_VALUE
        registerReceiver(smsBroadcastReceiver, filter);
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //处理任务
        return START_STICKY
    }

    private fun setForegroundService() {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.mipmap.ic_notification) //设置通知图标
            .setContentTitle(getString(R.string.app_name))//设置通知标题
            .setContentText("短信转发已启用")//设置通知内容
            .setAutoCancel(false) //用户触摸时，自动关闭
            .setOngoing(true);//设置处于运行状态
        startForeground(NOTIFICATION_ID, builder.build());
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //设定的通知渠道名称
            val channelName = "服务常驻通知"
            //设置通知的重要程度
            val importance = NotificationManager.IMPORTANCE_LOW
            //构建通知渠道
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel.description = "服务常驻通知"
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}

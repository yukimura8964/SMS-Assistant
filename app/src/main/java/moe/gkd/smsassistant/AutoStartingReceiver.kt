package moe.gkd.smsassistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 自动启动
 */
class AutoStartingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, SmsBroadcastService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}

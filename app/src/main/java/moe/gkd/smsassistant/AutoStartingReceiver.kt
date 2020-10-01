package moe.gkd.smsassistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import moe.gkd.smsassistant.helper.SharedPreferencesHelper

/**
 * 自动启动
 */
class AutoStartingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (SharedPreferencesHelper.Settings.isAutoStarting) {
            SmsBroadcastService.startService(context)
        }
    }
}

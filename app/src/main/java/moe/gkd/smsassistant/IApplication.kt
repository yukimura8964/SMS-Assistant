package moe.gkd.smsassistant

import android.app.Application
import moe.gkd.smsassistant.helper.SharedPreferencesHelper

class IApplication : Application() {
    companion object {
        lateinit var instance: IApplication
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        if (SharedPreferencesHelper.Settings.isAutoStarting) {
            SmsBroadcastService.startService(this)
        }
    }
}
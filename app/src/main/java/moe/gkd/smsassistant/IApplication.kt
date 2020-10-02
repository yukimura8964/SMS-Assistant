package moe.gkd.smsassistant

import android.app.Application
import android.os.Handler
import android.os.Looper
import moe.gkd.smsassistant.helper.SharedPreferencesHelper


class IApplication : Application() {
    companion object {
        lateinit var instance: IApplication
    }

    private lateinit var mainHandler: Handler

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (SharedPreferencesHelper.Settings.isAutoStarting) {
            SmsBroadcastService.startService(this)
        }
    }

    fun getMainHandler(): Handler {
        if (!this::mainHandler.isInitialized) {
            mainHandler = Handler(Looper.getMainLooper())
        }
        return mainHandler
    }
}
package moe.gkd.smsassistant.helper

import android.content.Context
import android.content.SharedPreferences
import moe.gkd.smsassistant.IApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 自定义委托
 */
object SharedPreferencesHelper {
    object Settings : Delegates() {
        override fun getSharedPreferencesName(): String = this::class.java.simpleName

        //是否开机自动启动
        var isAutoStarting by boolean()

        //是否启用邮件转发
        var isEnableEmailForward by boolean()
    }

    abstract class Delegates {
        private val preferences: SharedPreferences by lazy {
            IApplication.instance.applicationContext.getSharedPreferences(
                getSharedPreferencesName(),
                Context.MODE_PRIVATE
            )

        }

        fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<Any, Boolean> {
            override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
                preferences.edit().putBoolean(property.name, value).apply()
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
                return preferences.getBoolean(property.name, defaultValue)
            }

        }

        fun string(defaultValue: String? = null) = object : ReadWriteProperty<Any, String?> {
            override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
                preferences.edit().putString(property.name, value).apply()
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): String? {
                return preferences.getString(property.name, defaultValue)
            }
        }

        fun int(defaultValue: Int = 0) = object : ReadWriteProperty<Any, Int> {
            override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
                preferences.edit().putInt(property.name, value).apply()
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): Int {
                return preferences.getInt(property.name, defaultValue)
            }
        }

        abstract fun getSharedPreferencesName(): String

    }
}
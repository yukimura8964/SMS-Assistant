package moe.gkd.smsassistant.helper

import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {
    /**
     * 时间戳转字符串
     */
    fun Long.stamp2Str(format: String = "YYYY-M-d HH:mm"): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.CHINA)
            sdf.format(Date(this))
        } catch (e: Exception) {
            ""
        }
    }
}
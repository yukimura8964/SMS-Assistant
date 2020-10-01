package moe.gkd.smsassistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

class SmsBroadcastReceiver : BroadcastReceiver() {
    companion object {
        val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    }

    private val TAG = this::class.java.simpleName
    override fun onReceive(context: Context, intent: Intent) {
        if (SMS_RECEIVED == intent.action) {
            intent.extras?.let {
                readSms(it)
            }
        }
    }

    private fun readSms(bundle: Bundle) {
        val pdus: Array<Any> = bundle.get("pdus") as Array<Any>
        val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus.size)
        for (i in pdus.indices) {
            messages[i] = SmsMessage.createFromPdu(
                pdus[i] as ByteArray
            )
        }
        val msgBody = kotlin.run {
            val sb = StringBuffer()
            messages.forEach {
                sb.append(it!!.messageBody)
            }
            sb.toString()
        }
        val msgAddress = messages[0]!!.originatingAddress
        val msgDate = messages[0]!!.timestampMillis
        Log.e(TAG, "body = $msgBody address = $msgAddress date = $msgDate")
        // TODO: 2020/10/1 处理接收到的短信 
    }
}

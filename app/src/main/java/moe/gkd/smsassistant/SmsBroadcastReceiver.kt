package moe.gkd.smsassistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import moe.gkd.smsassistant.helper.EmailHelper
import moe.gkd.smsassistant.helper.SharedPreferencesHelper
import moe.gkd.smsassistant.helper.TimeHelper.stamp2Str

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
        val msgAddress = messages[0]!!.originatingAddress!!
        val msgDate = messages[0]!!.timestampMillis
        Log.e(TAG, "body = $msgBody address = $msgAddress date = $msgDate")
        if (SharedPreferencesHelper.Settings.isEnableEmailForward) {
            //如果开启了邮件转发
            forwardEmail(msgBody, msgAddress, msgDate)
        }
    }

    private fun forwardEmail(msgBody: String, msgAddress: String, msgDate: Long) {
        val content = "<body>\n" +
                "    <span style=\"color:black\">$msgBody</span>\n" +
                "    <hr>\n" +
                "    <p style=\"font-size: small; color: gray; text-align: right;\">$msgAddress</p>\n" +
                "    <p style=\"font-size: small;color: gray; text-align: right;\">${msgDate.stamp2Str()}</p>\n" +
                "</body>"
        EmailHelper.sendEmail(
            title = "短信转发通知",
            content = content,
            observer = object : SingleObserver<Unit> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: Unit) {
                    Log.e(TAG, "邮件发送成功")
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "邮件发送失败")
                }
            }
        )
    }
}

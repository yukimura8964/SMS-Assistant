package moe.gkd.smsassistant.helper

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.MimeMessage

object EmailHelper {

    private fun createSmtpSession(
        smtpPort: String,
    ): Session {
        val props = Properties()
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        props.setProperty("mail.smtp.socketFactory.fallback", "false")
        props.setProperty("mail.smtp.port", smtpPort)
        props.setProperty("mail.smtp.socketFactory.port", smtpPort)
        props.put("mail.smtp.auth", "true")
        val session = Session.getDefaultInstance(props)
        session.debug = true
        return session
    }

    fun sendEmail(
        title: String?,
        content: String?,
        toEmailAddress: String? = SharedPreferencesHelper.Settings.toEmailAddress,
        fromEmailAddress: String? = SharedPreferencesHelper.Settings.fromEmailAddress,
        smtpHost: String? = SharedPreferencesHelper.Settings.smtpHost,
        smtpPort: String? = SharedPreferencesHelper.Settings.smtpPort,
        username: String? = SharedPreferencesHelper.Settings.emailUsername,
        password: String? = SharedPreferencesHelper.Settings.emailPassword,
        isTest: Boolean,
        observer: SingleObserver<Unit>
    ) {
        Single.create(SingleOnSubscribe<Unit> { emitter ->
            try {
                if (title.isNullOrEmpty() ||
                    content.isNullOrEmpty() ||
                    toEmailAddress.isNullOrEmpty() ||
                    fromEmailAddress.isNullOrEmpty() ||
                    smtpHost.isNullOrEmpty() ||
                    smtpPort.isNullOrEmpty() ||
                    username.isNullOrBlank() ||
                    password.isNullOrBlank()
                ) {
                    throw Throwable("缺少参数")
                }
                val session = createSmtpSession(smtpHost)
                val message = MimeMessage(session)
                message.setFrom(fromEmailAddress);
                message.setRecipients(Message.RecipientType.TO, toEmailAddress)
                message.setSubject(title, "UTF-8")
                message.setText(content, "UTF-8", "html")
                val transport = session.getTransport("smtps")
                transport.connect(smtpHost, username, password);
                transport.sendMessage(message, message.allRecipients)
                transport.close()
                emitter.onSuccess(Unit)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(if (isTest) AndroidSchedulers.mainThread() else Schedulers.io())
            .subscribe(observer)
    }
}
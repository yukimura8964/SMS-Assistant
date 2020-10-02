package moe.gkd.smsassistant.helper

import java.security.Security
import java.util.*
import javax.mail.*
import javax.mail.internet.MimeMessage

object EmailHelper {

    private fun createSmtpSession(
        smtpHost: String,
        smtpPort: Int,
        username: String,
        password: String
    ): Session {
        val props = Properties()
        props.setProperty("mail.smtp.host",smtpHost)
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        props.setProperty("mail.smtp.socketFactory.fallback", "false")
        props.setProperty("mail.smtp.port", "$smtpPort")
        props.setProperty("mail.smtp.socketFactory.port", "$smtpPort")
        props.put("mail.smtp.auth", "true")
        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
        session.debug = true
        return session
    }

    @Throws(Exception::class)
    fun sendEmail(
        title: String?,
        content: String?,
        toEmailAddress: String? = SharedPreferencesHelper.Settings.toEmailAddress,
        fromEmailAddress: String? = SharedPreferencesHelper.Settings.fromEmailAddress,
        smtpHost: String? = SharedPreferencesHelper.Settings.smtpHost,
        smtpPort: Int? = SharedPreferencesHelper.Settings.smtpPort,
        username: String? = SharedPreferencesHelper.Settings.emailUsername,
        password: String? = SharedPreferencesHelper.Settings.emailPassword,
    ) {
        if (title.isNullOrEmpty() ||
            content.isNullOrEmpty() ||
            toEmailAddress.isNullOrEmpty() ||
            fromEmailAddress.isNullOrEmpty() ||
            smtpHost.isNullOrEmpty() ||
            (smtpPort == null || smtpPort == 0) ||
            username.isNullOrBlank() ||
            password.isNullOrBlank()
        ) {
            throw RuntimeException("缺少参数")
        }
        val session = createSmtpSession(smtpHost, smtpPort, username, password)
        val message = MimeMessage(session)
        message.setFrom(fromEmailAddress);
        message.setRecipients(Message.RecipientType.TO, toEmailAddress)
        message.setSubject(title, "UTF-8");
        message.setText(content, "UTF-8")
        Transport.send(message)
    }
}
package moe.gkd.smsassistant.ui.forward

import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sun.mail.util.MailConnectException
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityForwardConfigBinding
import moe.gkd.smsassistant.helper.EmailHelper
import moe.gkd.smsassistant.helper.SharedPreferencesHelper

class ForwardConfigActivity : BaseActivity<ActivityForwardConfigBinding>() {
    override fun getViewBinding(): ActivityForwardConfigBinding {
        return ActivityForwardConfigBinding.inflate(layoutInflater)
    }

    private val progressDialog by lazy {
        val dialog = AlertDialog.Builder(getContext())
            .setMessage("正在发送")
            .setCancelable(false)
            .create()
        dialog
    }

    override fun init() {
        initViews()
        loadData()
    }

    private fun initViews() {
        binding.emailForwardCheckbox.setOnCheckedChangeListener { _, isChecked ->
            showEmailForwardLayout(isChecked)
        }
        binding.emailTest.setOnClickListener {
            val toEmailAddress = toEmailAddress()
            val fromEmailAddress = fromEmailAddress()
            val smtpHost = smtpHost()
            val smtpPort = smtpPort()
            val emailUsername = emailUsername()
            val emailPassword = emailPassword()
            val content = "<p>Hi, 这是一封测试邮件</p>"
            EmailHelper.sendEmail(
                title = "测试标题",
                content = content,
                toEmailAddress = toEmailAddress,
                fromEmailAddress = fromEmailAddress,
                smtpHost = smtpHost,
                smtpPort = smtpPort,
                username = emailUsername,
                password = emailPassword,
                isTest = true,
                observer = object : SingleObserver<Unit> {
                    override fun onSubscribe(d: Disposable) {
                        progressDialog.show()
                    }

                    override fun onSuccess(t: Unit) {
                        progressDialog.dismiss()
                        AlertDialog.Builder(getContext())
                            .setTitle("发送成功")
                            .setMessage(content)
                            .setPositiveButton(
                                "确定"
                            ) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    override fun onError(e: Throwable) {
                        progressDialog.dismiss()
                        val str: String? = if (e is MailConnectException) {
                            e.localizedMessage
                        } else {
                            e.message
                        }
                        AlertDialog.Builder(getContext())
                            .setTitle("发送失败")
                            .setMessage(if (str.isNullOrEmpty()) "" else str)
                            .setPositiveButton(
                                "确定"
                            ) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            )
        }
    }

    private fun loadData() {
        binding.autoStartingCheckbox.isChecked = SharedPreferencesHelper.Settings.isAutoStarting
        binding.emailForwardCheckbox.isChecked =
            SharedPreferencesHelper.Settings.isEnableEmailForward
        binding.toEmailAddress.setText(SharedPreferencesHelper.Settings.toEmailAddress)
        binding.fromEmailAddress.setText(SharedPreferencesHelper.Settings.fromEmailAddress)
        binding.smtpHost.setText(SharedPreferencesHelper.Settings.smtpHost)
        binding.smtpPort.setText(SharedPreferencesHelper.Settings.smtpPort)
        binding.emailUsername.setText(SharedPreferencesHelper.Settings.emailUsername)
        binding.emailPassword.setText(SharedPreferencesHelper.Settings.emailPassword)
    }

    override fun onResume() {
        super.onResume()
        showEmailForwardLayout(binding.emailForwardCheckbox.isChecked)
    }

    private fun showEmailForwardLayout(isShow: Boolean) {
        if (isShow) {
            binding.emailForwardLayout.visibility = View.VISIBLE
        } else {
            binding.emailForwardLayout.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(getContext())
            .setTitle("提示")
            .setMessage("是否保存当前设置?")
            .setCancelable(false)
            .setNegativeButton("否") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton("是") { dialog, _ ->
                dialog.dismiss()
                saveConfig()
                finish()
            }
            .create()
        dialog.show()
    }

    private fun isAutoStarting(): Boolean = binding.autoStartingCheckbox.isChecked
    private fun isEnableEmailForward(): Boolean = binding.emailForwardCheckbox.isChecked
    private fun toEmailAddress(): String = binding.toEmailAddress.text.toString()
    private fun fromEmailAddress(): String = binding.fromEmailAddress.text.toString()
    private fun smtpHost(): String = binding.smtpHost.text.toString()
    private fun smtpPort(): String = binding.smtpPort.text.toString()
    private fun emailUsername(): String = binding.emailUsername.text.toString()
    private fun emailPassword(): String = binding.emailPassword.text.toString()

    private fun saveConfig() {
        SharedPreferencesHelper.Settings.isAutoStarting = isAutoStarting()
        SharedPreferencesHelper.Settings.isEnableEmailForward = isEnableEmailForward()
        SharedPreferencesHelper.Settings.toEmailAddress = toEmailAddress()
        SharedPreferencesHelper.Settings.fromEmailAddress = fromEmailAddress()
        SharedPreferencesHelper.Settings.smtpHost = smtpHost()
        SharedPreferencesHelper.Settings.smtpPort = smtpPort()
        SharedPreferencesHelper.Settings.emailUsername = emailUsername()
        SharedPreferencesHelper.Settings.emailPassword = emailPassword()
    }
}
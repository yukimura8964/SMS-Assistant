package moe.gkd.smsassistant.ui.forward

import android.view.View
import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityForwardConfigBinding
import moe.gkd.smsassistant.helper.SharedPreferencesHelper

class ForwardConfigActivity : BaseActivity<ActivityForwardConfigBinding>() {
    override fun getViewBinding(): ActivityForwardConfigBinding {
        return ActivityForwardConfigBinding.inflate(layoutInflater)
    }

    override fun init() {
        initViews()
        loadData()
    }

    private fun initViews() {
        binding.emailForwardCheckbox.setOnCheckedChangeListener { _, isChecked ->
            showEmailForwardLayout(isChecked)
        }
    }

    private fun loadData() {
        binding.autoStartingCheckbox.isChecked = SharedPreferencesHelper.Settings.isAutoStarting
        binding.emailForwardCheckbox.isChecked =
            SharedPreferencesHelper.Settings.isEnableEmailForward
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
}
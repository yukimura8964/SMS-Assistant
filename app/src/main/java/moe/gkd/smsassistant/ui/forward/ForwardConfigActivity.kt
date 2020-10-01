package moe.gkd.smsassistant.ui.forward

import android.view.View
import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityForwardConfigBinding

class ForwardConfigActivity : BaseActivity<ActivityForwardConfigBinding>() {
    override fun getViewBinding(): ActivityForwardConfigBinding {
        return ActivityForwardConfigBinding.inflate(layoutInflater)
    }

    override fun init() {
        initViews()
    }

    private fun initViews() {
        binding.emailForwardCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.emailForwardLayout.visibility = View.VISIBLE
            } else {
                binding.emailForwardLayout.visibility = View.GONE
            }
        }
    }
}
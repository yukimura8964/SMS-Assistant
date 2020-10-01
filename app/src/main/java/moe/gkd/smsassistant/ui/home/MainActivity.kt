package moe.gkd.smsassistant.ui.home

import android.content.Intent
import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityMainBinding
import moe.gkd.smsassistant.ui.forward.ForwardConfigActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun init() {
        initViews()
    }

    private fun initViews() {
        binding.forwardConfigCard.setOnClickListener {
            val intent = Intent(getContext(), ForwardConfigActivity::class.java)
            startActivity(intent)
        }
    }
}
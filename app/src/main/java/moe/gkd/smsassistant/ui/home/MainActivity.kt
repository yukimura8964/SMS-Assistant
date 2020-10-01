package moe.gkd.smsassistant.ui.home

import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun init() {

    }
}
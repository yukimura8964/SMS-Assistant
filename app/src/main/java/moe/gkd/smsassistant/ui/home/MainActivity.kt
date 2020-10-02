package moe.gkd.smsassistant.ui.home

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityMainBinding
import moe.gkd.smsassistant.entity.ConfigStatusEntity
import moe.gkd.smsassistant.helper.SharedPreferencesHelper
import moe.gkd.smsassistant.ui.forward.ForwardConfigActivity
import moe.gkd.smsassistant.ui.home.adapter.ConfigListAdapter

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val configList = ArrayList<ConfigStatusEntity>()
    private val configListAdapter = ConfigListAdapter(configList)

    override fun init() {
        initViews()
    }

    private fun initViews() {
        binding.forwardConfigCard.setOnClickListener {
            val intent = Intent(getContext(), ForwardConfigActivity::class.java)
            startActivity(intent)
        }
        binding.configRecycler.apply {
            setOnTouchListener { v, event ->
                binding.forwardConfigCard.onTouchEvent(event)
            }
            layoutManager = LinearLayoutManager(context)
            adapter = configListAdapter

        }
    }

    override fun onResume() {
        super.onResume()
        configList.apply {
            clear()
            add(ConfigStatusEntity("自动运行", SharedPreferencesHelper.Settings.isAutoStarting))
            add(ConfigStatusEntity("邮件转发", SharedPreferencesHelper.Settings.isEnableEmailForward))
        }
        configListAdapter.notifyDataSetChanged()
    }
}
package moe.gkd.smsassistant.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moe.gkd.smsassistant.IApplication
import moe.gkd.smsassistant.R
import moe.gkd.smsassistant.SmsBroadcastService
import moe.gkd.smsassistant.base.BaseActivity
import moe.gkd.smsassistant.databinding.ActivityMainBinding
import moe.gkd.smsassistant.entity.ConfigStatusEntity
import moe.gkd.smsassistant.event.ForwardSuccessEvent
import moe.gkd.smsassistant.event.SmsBroadcastServiceStatusEvent
import moe.gkd.smsassistant.helper.SharedPreferencesHelper
import moe.gkd.smsassistant.sql.AppDatabase
import moe.gkd.smsassistant.ui.forward.ForwardConfigActivity
import moe.gkd.smsassistant.ui.home.adapter.ConfigListAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val configList = ArrayList<ConfigStatusEntity>()
    private val configListAdapter = ConfigListAdapter(configList)

    override fun init() {
        registerEventBus()
        initViews()
    }


    override fun onDestroy() {
        unRegisterEventBus()
        super.onDestroy()
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
        binding.switchCard.setOnClickListener {
            if (SmsBroadcastService.isRunning) {
                SmsBroadcastService.stopService(getContext())
            } else {
                rxPermissions.request(Manifest.permission.RECEIVE_SMS)
                    .subscribe {
                        if (it) {
                            SmsBroadcastService.startService(getContext())
                        } else {
                            AlertDialog.Builder(getContext())
                                .setTitle("没有短信权限")
                                .show()
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadConfigList()
        loadServiceStatus()
        loadForwardCount()
    }

    private fun loadConfigList() {
        configList.apply {
            clear()
            add(ConfigStatusEntity("自动运行", SharedPreferencesHelper.Settings.isAutoStarting))
            add(ConfigStatusEntity("邮件转发", SharedPreferencesHelper.Settings.isEnableEmailForward))
        }
        configListAdapter.notifyDataSetChanged()
    }

    private fun loadServiceStatus(isRunning: Boolean = SmsBroadcastService.isRunning) {
        if (isRunning) {
            if (lacksPermission(Manifest.permission.RECEIVE_SMS)) {
                //没有权限
                binding.statusIcon.setImageResource(R.mipmap.ic_status_error)
                binding.statusText.text = "没有短信权限"
                binding.statusText.setTextColor(Color.RED)
            } else {
                binding.statusIcon.setImageResource(R.mipmap.ic_status_success)
                binding.statusText.text = "正在运行"
                binding.statusText.setTextColor(Color.GREEN)
            }
        } else {
            binding.statusIcon.setImageResource(R.mipmap.ic_status_stop)
            binding.statusText.text = "停止运行"
            binding.statusText.setTextColor(Color.GRAY)
        }
    }

    private fun loadForwardCount() {
        AppDatabase.getInstance().forwardLogDao().getCount().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: Int) {
                    binding.forwardCount.text = "$t"
                }

                override fun onError(e: Throwable) {
                }

            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onServiceStatusEvent(event: SmsBroadcastServiceStatusEvent) {
        loadServiceStatus(event.isRunning)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onForwardSuccessEvent(event: ForwardSuccessEvent) {
        loadForwardCount()
    }
}
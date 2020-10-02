package moe.gkd.smsassistant.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.tbruyelle.rxpermissions2.RxPermissions
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity<Binding : ViewBinding> : AppCompatActivity() {
    protected val TAG = this::class.java.simpleName

    protected val rxPermissions: RxPermissions by lazy {
        RxPermissions(this@BaseActivity)
    }

    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        init()
    }

    protected abstract fun getViewBinding(): Binding

    protected abstract fun init()

    protected fun getContext(): Context = this

    protected fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    protected fun unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 判断是否缺少权限
     */
    protected fun lacksPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(getContext(), permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
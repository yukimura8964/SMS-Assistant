package moe.gkd.smsassistant.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tbruyelle.rxpermissions2.RxPermissions

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

}
package com.mouradelamrani.salaam.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.ui.base.BaseActivity
import com.mouradelamrani.salaam.ui.main.MainActivity

class SplashActivity : BaseActivity() {

    override val layoutId: Int? = R.layout.activity_splash

    private var handler: Handler? = null

    private var started: Boolean = false
    private var launchOnStart = false


    override fun onStart() {
        super.onStart()
        started = true

        when {
            launchOnStart -> launchMainActivity()
            handler == null -> {
                handler = Handler(Looper.getMainLooper())
                handler?.postDelayed({
                    if (started) launchMainActivity()
                    else launchOnStart = true
                }, 2000)
            }
        }
    }

    override fun onStop() {
        started = false
        super.onStop()
    }


    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.splash_start_enter, R.anim.splash_start_exit)
        finish()
    }


}
package com.portalpirates.cufit.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.ImageView
import com.portalpirates.cufit.FitActivity
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.user.login.LoginActivity

class SplashActivity : FitActivity() {

    private val handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        window.exitTransition = null
        window.addFlags(Window.FEATURE_ACTIVITY_TRANSITIONS)

        handler.postDelayed({
            val logoView = findViewById<ImageView>(R.id.logo)
            val intent = Intent(this, LoginActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, logoView, "logo")
            startActivity(intent, options.toBundle())
        }, DELAY_MS)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        const val DELAY_MS = 2500L
    }
}
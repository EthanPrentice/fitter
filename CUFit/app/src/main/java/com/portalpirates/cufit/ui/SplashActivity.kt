package com.portalpirates.cufit.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.portalpirates.cufit.FitActivity
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.user.auth.AuthActivity

class SplashActivity : FitActivity() {

    private val handler = Handler(Looper.myLooper()!!)

    init {
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.splash_layout)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        handler.postDelayed({
            val logoView = findViewById<ImageView>(R.id.add_photo_btn)
            val intent = Intent(this, AuthActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, logoView, resources.getString(R.string.tr_logo))

            window.exitTransition = null

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
package com.portalpirates.cufit.ui.user.login

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.portalpirates.cufit.FitActivity
import com.portalpirates.cufit.R


class LoginActivity : FitActivity() {

    lateinit var fragContainer: FrameLayout

    init {
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frag_only_layout)

        postponeEnterTransition()
        window.enterTransition = null

        fragContainer = findViewById(R.id.frag_container)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        if (savedInstanceState == null) {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.frag_container, LoginFragment.newInstance(), LoginFragment.TAG)
            transaction.commit()
        }
    }

}
package com.ethanprentice.fitter.ui.user.auth

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ethanprentice.fitter.ui.FitActivity
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.user.AuthenticatedUser
import com.ethanprentice.fitter.ui.nav.NavActivity
import com.ethanprentice.fitter.ui.user.welcome.WelcomeActivity


class AuthActivity : FitActivity(), SignUpFragment.SignUpListener, LoginFragment.LogInListener {

    // use to finish the activity post-transition in onStop
    private var shouldFinish = false

    init {
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.enterTransition = null
        window.exitTransition = null

        setContentView(R.layout.frag_only_layout)

        postponeEnterTransition()

        fragContainer = findViewById(R.id.frag_container)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putBoolean(HAS_ACTIVITY_SHARED_ELEM_TRANSITION, true)

            val frag = LoginFragment.newInstance(bundle)

            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.frag_container, frag, LoginFragment.TAG)
            transaction.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        if (shouldFinish) {
            finish()
        }
    }

    override fun onSignUp(uid: String) {
        val intent = Intent(this, WelcomeActivity::class.java)

        val logo = findViewById<View>(R.id.logo)
        val actionBtn = findViewById<View>(R.id.action_btn)

        val sharedElements = arrayOf<Pair<View, String>>(
            Pair.create(logo, resources.getString(R.string.tr_logo)),
            Pair.create(actionBtn, resources.getString(R.string.tr_action_btn))
        )

        // prevent flashing for shared element transition
        window.exitTransition = null

        val options = ActivityOptions.makeSceneTransitionAnimation(this, *sharedElements)
        startActivity(intent, options.toBundle())
        shouldFinish = true
    }

    override fun onLogIn(authUser: AuthenticatedUser) {
        val intent = Intent(this, NavActivity::class.java)
        startActivity(intent)
        finish()
    }
}
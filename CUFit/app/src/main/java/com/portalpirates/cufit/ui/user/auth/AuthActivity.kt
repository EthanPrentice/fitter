package com.portalpirates.cufit.ui.user.auth

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.portalpirates.cufit.ui.FitActivity
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.cloud.TaskListener
import com.portalpirates.cufit.datamodel.data.user.AuthenticatedUser
import com.portalpirates.cufit.datamodel.manager.UserManager
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.nav.NavActivity
import com.portalpirates.cufit.ui.user.welcome.WelcomeActivity


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
        launchWelcomeFlow()
    }

    override fun onLogIn(userManager: UserManager, authUser: AuthenticatedUser) {
        val listener = object : TaskListener<Boolean> {
            override fun onSuccess(value: Boolean) {
                if (value) {
                    // val intent = Intent(this, UserActivity::class.java)
                    // startActivity(intent)
                    // finish()
                    Toast.makeText(
                        FitApplication.instance.applicationContext,
                        "Authenticated as ${authUser.fullName}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        FitApplication.instance.applicationContext,
                        "Required fields are missing for ${authUser.fullName}",
                        Toast.LENGTH_LONG
                    ).show()
                    launchWelcomeFlow()
                }
            }

            override fun onFailure(e: Exception?) {
                // TODO - will handle UserCloudInterface onFailure case; will this present message to the user? How?
            }
        }

        userManager.provider.userFinishedWelcomeFlow(listener)
    }

    private fun launchWelcomeFlow() {
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
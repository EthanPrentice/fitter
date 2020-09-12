package com.portalpirates.cufit.ui.user.login

import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.FitButton

class LoginFragment : FitFragment() {

    private lateinit var root: ConstraintLayout

    private lateinit var loginCreds: LinearLayout
    private lateinit var loginBtn: FitButton
    private lateinit var signupBtn: FitButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.login_layout, container, false) as ConstraintLayout

        loginCreds = root.findViewById(R.id.login_credentials)
        loginBtn = root.findViewById(R.id.login_btn)
        signupBtn = root.findViewById(R.id.sign_up_btn)

        if (savedInstanceState == null) {
            loginCreds.alpha = 0f
            loginBtn.alpha = 0f
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        requireActivity().startPostponedEnterTransition()

        val sharedElementEnterTransition = fitActivity!!.window.sharedElementEnterTransition
        sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition) { }
            override fun onTransitionPause(transition: Transition) { }
            override fun onTransitionCancel(transition: Transition) { }
            override fun onTransitionStart(transition: Transition) { }

            override fun onTransitionEnd(transition: Transition) {
                loginCreds.fadeIn()
                loginBtn.fadeIn()
                signupBtn.fadeIn()
            }
        })
    }

    private fun View.fadeIn() {
        alpha = 0f
        visibility = View.VISIBLE

        animate().alpha(1f)
            .setDuration(400L)
            .start()
    }

    companion object {
        const val TAG = "LoginFragment"
    }

}
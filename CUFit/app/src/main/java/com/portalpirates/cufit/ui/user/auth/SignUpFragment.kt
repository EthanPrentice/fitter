package com.portalpirates.cufit.ui.user.auth

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.user.welcome.WelcomeActivity
import android.util.Pair as UtilPair

class SignUpFragment : AuthFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPostponedEnterTransition()
    }

    override fun setImeListeners() {
        confirmPasswordInput.editText?.setOnEditorActionListener listener@{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                signUp()
                return@listener true
            }
            return@listener false
        }
    }

    override fun setActionOnClickListener() {
        actionBtn.setOnClickListener {
            signUp()
        }
    }

    override fun setSwitchModeOnClickListener() {
        switchModeBtn.setOnClickListener {
            swapAuthMode(AuthMode.LOG_IN)
        }
    }


    /**
     * Attempts to sign up a user in based on the information filled out in the UI
     */
    private fun signUp() {
        val email = emailAddrInput.text
        val password = passwordInput.text
        val confirmPassword = confirmPasswordInput.text
        
        if (password != confirmPassword) {
            onIncorrectInput()
            return
        }

        // Put userManager in the view model later when it's written
        val userManager = FitApplication.instance.userManager
        userManager.receiver.signUpUser(email, password) { success ->
            if (success) {
                val fbUser = userManager.provider.getFirebaseUser()
                Toast.makeText(context, "Authenticated as user with uid ${fbUser?.uid ?: -1}", Toast.LENGTH_SHORT).show()

                // we can't get an auth user yet, the required fields aren't populated.
//                userManager.provider.getAuthenticatedUser { user ->
//                    if (user == null) {
//                        onIncorrectInput()
//                    } else {
//                        Toast.makeText(context, "Authenticated as ${user.fullName}", Toast.LENGTH_SHORT).show()
//                    }
//                }
            } else {
                onIncorrectInput()
            }
        }
    }

    companion object {
        const val TAG = "SignUpFragment"

        fun newInstance(bundle: Bundle? = null): SignUpFragment {
            val frag = SignUpFragment()
            frag.arguments = bundle
            return frag
        }
    }

}
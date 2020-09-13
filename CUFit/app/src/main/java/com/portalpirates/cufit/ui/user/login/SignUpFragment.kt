package com.portalpirates.cufit.ui.user.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.portalpirates.cufit.R

class SignUpFragment private constructor() : AuthFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPostponedEnterTransition()
    }

    override fun setImeListeners() {
        confirmPasswordInput.editText?.setOnEditorActionListener listener@{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
        onIncorrectInput()
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
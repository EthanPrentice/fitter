package com.ethanprentice.fitter.ui.user.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.ui.FitApplication
import com.ethanprentice.fitter.viewmodel.AuthViewModel

class SignUpFragment : AuthFragment() {

    val listener: SignUpListener?
        get() = activity as? SignUpListener

    val model: AuthViewModel by activityViewModels()

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
            showMessage("Passwords do not match")
            return
        }

        // Put userManager in the view model later when it's written
        model.signUpUser(email, password, object :
            TaskListener<Unit?> {
            override fun onSuccess(value: Unit?) {
                hideMessage()

                model.firebaseUser?.let {
                    listener?.onSignUp(it.uid)
                    hideMessage()
                }
            }
          
            override fun onFailure(e: Exception?) {
                onIncorrectInput()
                hideMessage()
                e?.message?.let { msg ->
                    showMessage(msg)
                }
            }
        })
    }

    interface SignUpListener {
        fun onSignUp(uid: String)
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
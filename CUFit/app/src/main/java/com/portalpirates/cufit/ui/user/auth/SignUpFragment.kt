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
import com.portalpirates.cufit.R
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
        val intent = Intent(requireContext(), WelcomeActivity::class.java)

        val sharedElements = arrayOf<UtilPair<View, String>>(
            UtilPair.create(logo, resources.getString(R.string.tr_logo)),
            UtilPair.create(actionBtn, resources.getString(R.string.tr_action_btn))
        )

        // prevent flashing for shared element transition
        requireActivity().window.exitTransition = null

        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), *sharedElements)
        startActivity(intent, options.toBundle())

        // onIncorrectInput()
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
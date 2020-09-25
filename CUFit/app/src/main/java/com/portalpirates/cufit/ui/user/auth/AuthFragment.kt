package com.portalpirates.cufit.ui.user.auth

import android.os.Bundle
import android.transition.*
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.FitButton
import com.portalpirates.cufit.ui.view.FitEditText

abstract class AuthFragment : FitFragment() {

    protected enum class AuthMode {
        LOG_IN,
        SIGN_UP
    }

    protected lateinit var logo: ImageView

    protected lateinit var userInputs: LinearLayout
    protected lateinit var emailAddrInput: FitEditText
    protected lateinit var passwordInput: FitEditText
    protected lateinit var confirmPasswordInput: FitEditText

    protected lateinit var message: TextView
    protected lateinit var actionBtn: FitButton

    protected lateinit var bottomTray: LinearLayout
    protected lateinit var switchModeBtn: FitButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        // don't save passwords here
        try {
            savedInstanceState.putString(BUNDLE_EMAIL_TEXT, emailAddrInput.text)
        }
        catch (e: UninitializedPropertyAccessException) {
            Log.w(TAG, e.message ?: "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logo = view.findViewById(R.id.logo)

        userInputs = view.findViewById(R.id.credentials)
        emailAddrInput = userInputs.findViewById(R.id.email_input)
        passwordInput = userInputs.findViewById(R.id.password_input)
        confirmPasswordInput = userInputs.findViewById(R.id.confirm_password_input)

        message = view.findViewById(R.id.message)
        actionBtn = view.findViewById(R.id.action_btn)

        bottomTray = view.findViewById(R.id.bottom_tray)
        switchModeBtn = bottomTray.findViewById(R.id.switch_mode_btn)

        // Let the users email persist between recreates or from passed in bundles
        if (savedInstanceState != null) {
            // pull data from savedInstanceState in-case we get recreated
            emailAddrInput.text = savedInstanceState.getString(BUNDLE_EMAIL_TEXT) ?: ""
        }
        else {
            arguments?.let { args ->
                emailAddrInput.text = args.getString(BUNDLE_EMAIL_TEXT) ?: ""
            }
        }

        setImeListeners()
        setActionOnClickListener()
        setSwitchModeOnClickListener()
    }

    /**
     * Add any ImeAction listeners to the inputs
     */
    protected abstract fun setImeListeners()

    /**
     * Add listener to the action button
     */
    protected abstract fun setActionOnClickListener()

    /**
     * Add listener to the switch mode button
     */
    protected abstract fun setSwitchModeOnClickListener()

    protected open fun onIncorrectInput() {
        userInputs.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
    }

    /**
     * Transitions between auth modes (ie. login vs sign-up)
     */
    protected fun swapAuthMode(swapTo: AuthMode) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_EMAIL_TEXT, emailAddrInput.text)

        val frag = when (swapTo) {
            AuthMode.LOG_IN -> LoginFragment.newInstance(bundle)
            AuthMode.SIGN_UP -> SignUpFragment.newInstance(bundle)
        }

        val transition = TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeClipBounds())
            addTransition(ChangeTransform())
            addTransition(ChangeImageTransform())

            ordering = TransitionSet.ORDERING_TOGETHER
            duration = FRAG_TRANSITION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }

        frag.sharedElementEnterTransition = transition
        frag.sharedElementReturnTransition = transition

        requireActivity().supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(logo, resources.getString(R.string.tr_logo))
            .addSharedElement(userInputs, resources.getString(R.string.tr_credentials))
            .addSharedElement(emailAddrInput, resources.getString(R.string.tr_email_input))
            .addSharedElement(passwordInput, resources.getString(R.string.tr_password_input))
            .addSharedElement(confirmPasswordInput, resources.getString(R.string.tr_confirm_password_input))
            .addSharedElement(message, resources.getString(R.string.tr_message))
            .addSharedElement(actionBtn, resources.getString(R.string.tr_action_btn))
            .addSharedElement(bottomTray, resources.getString(R.string.tr_bottom_tray))
            .addSharedElement(switchModeBtn, resources.getString(R.string.tr_switch_mode_btn))
            .replace(R.id.frag_container, frag, TAG)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val TAG = "AuthFragment"

        protected const val BUNDLE_EMAIL_TEXT = "com.portalpirates.cufit.ui.user.auth.AuthFragment:email"
    }

}
package com.portalpirates.cufit.ui.user.login

import android.content.Context
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.animation.ResizeAnimation
import com.portalpirates.cufit.ui.view.FitButton
import kotlinx.android.synthetic.main.button_layout.view.*


class LoginFragment private constructor() : AuthFragment() {

    private var enterAnimationFinished = false
    private var wasPaused = false

    private val isForgotPasswordShowing: Boolean
        get() = forgotPasswordBtn.visibility == View.VISIBLE

    private lateinit var forgotPasswordBtn: FitButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotPasswordBtn = view.findViewById(R.id.forgot_password_btn)
        forgotPasswordBtn.btn_text_view.maxLines = 1

        startPostponedEnterTransition()

        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // if there is a sharedElement transition, fade in the views once it's finished
        val sharedElementEnterTransition = fitActivity!!.window.sharedElementEnterTransition
        sharedElementEnterTransition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition) { }
            override fun onTransitionPause(transition: Transition) { }
            override fun onTransitionStart(transition: Transition) {
                userInputs.alpha = 0f
                actionBtn.alpha = 0f
                switchModeBtn.alpha = 0f
            }

            override fun onTransitionEnd(transition: Transition) {
                enterAnimationFinished = true
                userInputs.fadeIn()
                actionBtn.fadeIn()
                switchModeBtn.fadeIn()
            }
            override fun onTransitionCancel(transition: Transition) = onTransitionEnd(transition)
        })
    }

    override fun onResume() {
        super.onResume()
        wasPaused = false

        // if the frag is resumed and the enter animation never finished, fade in the views
        if (!enterAnimationFinished && wasPaused) {
            enterAnimationFinished = true
            userInputs.fadeIn()
            actionBtn.fadeIn()
            switchModeBtn.fadeIn()
        }
    }

    override fun onPause() {
        super.onPause()
        wasPaused = true
    }

    override fun setActionOnClickListener() {
        actionBtn.setOnClickListener {
            login()
        }
    }

    override fun setSwitchModeOnClickListener() {
        switchModeBtn.setOnClickListener {
            swapAuthMode(AuthMode.SIGN_UP)
        }
    }

    override fun setImeListeners() {
        passwordInput.editText?.setOnEditorActionListener listener@{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                return@listener true
            }
            return@listener false
        }
    }

    private fun login() {
        onIncorrectInput()
    }

    override fun onIncorrectInput() {
        super.onIncorrectInput()
        showForgotPassword()
    }

    private fun hideForgotPassword() {
        if (!isForgotPasswordShowing) {
            return
        }
        val anim = ResizeAnimation(forgotPasswordBtn, 0, forgotPasswordBtn.measuredWidth, ResizeAnimation.Mode.WIDTH).apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) { }
                override fun onAnimationStart(p0: Animation?) { }
                override fun onAnimationEnd(p0: Animation?) {
                    forgotPasswordBtn.visibility = View.GONE
                }
            })
        }
        forgotPasswordBtn.startAnimation(anim)
    }

    private fun showForgotPassword() {
        if (isForgotPasswordShowing) {
            return
        }
        val anim = ResizeAnimation(forgotPasswordBtn, 0.5f, 0f, ResizeAnimation.Mode.WEIGHT).apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) { }
                override fun onAnimationEnd(p0: Animation?) { }
                override fun onAnimationStart(p0: Animation?) {
                    forgotPasswordBtn.visibility = View.VISIBLE
                }
            })
        }

        forgotPasswordBtn.startAnimation(anim)
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

        fun newInstance(bundle: Bundle? = null): LoginFragment {
            val frag = LoginFragment()
            frag.arguments = bundle
            return frag
        }
    }

}
package com.ethanprentice.fitter.ui.user.auth

import android.content.Context
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.user.AuthenticatedUser
import com.ethanprentice.fitter.ui.FitApplication
import com.ethanprentice.fitter.ui.animation.ResizeAnimation
import com.ethanprentice.fitter.ui.view.FitButton
import com.ethanprentice.fitter.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.button_layout.view.*
import java.lang.IllegalStateException


class LoginFragment : AuthFragment() {

    private var activityTransitionEnded = false

    private val isForgotPasswordShowing: Boolean
        get() = forgotPasswordBtn.visibility == View.VISIBLE

    private lateinit var forgotPasswordBtn: FitButton

    private var listener: LogInListener? = null

    private val model: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            requireActivity().startPostponedEnterTransition()
        }
        startPostponedEnterTransition()

        if (hasActivitySharedElemTransition && !activityTransitionEnded) {
            userInputs.alpha = 0f
            actionBtn.alpha = 0f
            bottomTray.alpha = 0f
        }

        forgotPasswordBtn = view.findViewById(R.id.forgot_password_btn)
        forgotPasswordBtn.btn_text_view.maxLines = 1
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().let {
            if (it is LogInListener) {
                listener = it
            } else {
                Log.e(TAG, "Activity must implement LogInListener")
                throw IllegalStateException("Activity must implement LogInListener")
            }
        }


        // fade views in once the sharedElements are done being animated
        val sharedElementEnterTransition = fitActivity!!.window.sharedElementEnterTransition
        sharedElementEnterTransition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition) { }
            override fun onTransitionPause(transition: Transition) { }
            override fun onTransitionStart(transition: Transition) { }

            override fun onTransitionEnd(transition: Transition) {
                activityTransitionEnded = true
                fun View.fadeIn() = animate().alpha(1f).setDuration(400L).start()
                userInputs.fadeIn()
                actionBtn.fadeIn()
                bottomTray.fadeIn()
            }
            override fun onTransitionCancel(transition: Transition) = onTransitionEnd(transition)
        })
    }

    override fun onPause() {
        super.onPause()

        // Transition ends and does not update the listener on-pause
        // If the fragment is paused and the transition has not finished, show the views
        if (hasActivitySharedElemTransition && !activityTransitionEnded) {
            userInputs.alpha = 1f
            actionBtn.alpha = 1f
            bottomTray.alpha = 1f
        }
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
            if (actionId == EditorInfo.IME_ACTION_GO) {
                login()
                return@listener true
            }
            return@listener false
        }
    }

    /**
     * Attempts to log in the user based on the information filled out in the UI
     */
    private fun login() {
        val email = emailAddrInput.text
        val password = passwordInput.text

        val listener = object :
            TaskListener<AuthenticatedUser?> {
            override fun onSuccess(value: AuthenticatedUser?) {
                hideMessage()
                if (value == null) {
                    onIncorrectInput()
                } else {
                    hideForgotPassword()
                    listener?.onLogIn(value)
                }
            }

            override fun onFailure(e: Exception?) {
                onIncorrectInput()
                hideMessage()
                e?.message?.let { msg ->
                    showMessage(msg)
                }
            }
        }

        model.authenticateUser(email, password, object :
            TaskListener<Unit?> {
            override fun onSuccess(value: Unit?) {
                model.getAuthenticatedUser(listener)
            }

            override fun onFailure(e: Exception?) {
                listener.onFailure(e)
            }
        })
    }

    override fun onIncorrectInput() {
        super.onIncorrectInput()
        showForgotPassword()
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

    private fun hideForgotPassword() {
        if (!isForgotPasswordShowing) {
            return
        }
        val anim = ResizeAnimation(forgotPasswordBtn, 0f, 0.5f, ResizeAnimation.Mode.WEIGHT).apply {
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

    interface LogInListener {
        fun onLogIn(authUser: AuthenticatedUser)
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
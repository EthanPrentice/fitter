package com.ethanprentice.fitter.ui.user.welcome

import android.content.Context
import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.user.FitUserBuilder
import com.ethanprentice.fitter.ui.FitFragment
import com.ethanprentice.fitter.ui.view.ChooseImageButton
import com.ethanprentice.fitter.ui.view.FitButton
import com.ethanprentice.fitter.viewmodel.WelcomeViewModel
import java.lang.IllegalStateException

abstract class WelcomeFragment : FitFragment() {

    protected lateinit var actionBtn: FitButton
    protected var topText: TextView? = null
    protected var chooseImageButton: ChooseImageButton? = null

    protected val listener: WelcomeFragListener?
        get() = activity as? WelcomeFragListener

    protected val model: WelcomeViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topText = view.findViewById(R.id.top_text)

        actionBtn = view.findViewById(R.id.action_btn)
        actionBtn.setOnClickListener {
            onActionClicked()
        }

        chooseImageButton = view.findViewById(R.id.choose_photo_btn)
        chooseImageButton?.setOnClearListener { _ ->
            model.setUserImage(null)
        }

        model.userImage.observe(requireActivity(), Observer { bmp ->
            if (bmp != null) {
                chooseImageButton?.setImageBitmap(bmp)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is WelcomeFragListener) {
            throw IllegalStateException("Attached activity must implement WelcomeFragListener")
        }
    }

    protected fun getWelcomeTransaction(frag: WelcomeFragment, tag: String): FragmentTransaction {
        val transition = AutoTransition().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            duration = FRAG_TRANSITION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }

        frag.sharedElementEnterTransition = transition
        frag.sharedElementReturnTransition = transition

        val transaction = requireActivity().supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(actionBtn, resources.getString(R.string.tr_action_btn))
            .replace(R.id.frag_container, frag, tag)
            .addToBackStack(null)

        chooseImageButton?.let { btn ->
            transaction.addSharedElement(btn, resources.getString(R.string.tr_choose_photo_btn))
            transaction.addSharedElement(btn.imageView, btn.imageView.transitionName)
            transaction.addSharedElement(btn.editBtn, btn.editBtn.transitionName)
        }

        topText?.let {
            transaction.addSharedElement(it, resources.getString(R.string.tr_top_text))
        }

        return transaction
    }

    protected abstract fun onActionClicked()


    interface WelcomeFragListener {
        fun userReadyToBuild(builder: FitUserBuilder)
    }
}
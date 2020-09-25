package com.portalpirates.cufit.ui.user.welcome

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.user.FitUser
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.FitButton
import kotlinx.android.synthetic.main.welcome_sex.view.*
import java.lang.IllegalStateException

abstract class WelcomeFragment : FitFragment() {

    protected lateinit var actionBtn: FitButton
    protected var topText: TextView? = null

    protected val listener: WelcomeFragListener?
        get() = activity as? WelcomeFragListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBtn = view.findViewById(R.id.action_btn)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is WelcomeFragListener) {
            throw IllegalStateException("Attached activity must implement WelcomeFragListener")
        }
    }


    interface WelcomeFragListener {
        fun userReadyToBuild(builder: FitUserBuilder)
    }
}
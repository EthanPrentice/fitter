package com.portalpirates.cufit.ui.user.welcome

import android.widget.TextView
import com.portalpirates.cufit.ui.FitFragment
import com.portalpirates.cufit.ui.view.FitButton

abstract class WelcomeFragment : FitFragment() {

    protected lateinit var actionBtn: FitButton
    protected var topText: TextView? = null

}
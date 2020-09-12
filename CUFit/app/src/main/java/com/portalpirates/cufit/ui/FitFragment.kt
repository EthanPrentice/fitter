package com.portalpirates.cufit.ui

import androidx.fragment.app.Fragment
import com.portalpirates.cufit.FitActivity

abstract class FitFragment : Fragment() {

    protected val fitActivity: FitActivity?
        get() = activity as? FitActivity

}
package com.portalpirates.cufit.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitFragment

class MyProfileFragment : FitFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_profile_layout, container, false)
    }

    companion object {
        const val TAG = "MyProfileFragment"

        fun newInstance(b: Bundle? = null): MyProfileFragment {
            val frag = MyProfileFragment()
            frag.arguments = b
            return frag
        }
    }

}
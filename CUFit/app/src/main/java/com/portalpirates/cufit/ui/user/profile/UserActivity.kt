package com.portalpirates.cufit.ui.user.profile

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.portalpirates.cufit.FitActivity
import com.portalpirates.cufit.R

class UserActivity : FitActivity() {


    private lateinit var fragContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = null

        postponeEnterTransition()
        setContentView(R.layout.main_layout)

        fragContainer = findViewById(R.id.frag_container)


        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putBoolean(HAS_ACTIVITY_SHARED_ELEM_TRANSITION, false)

            val frag = MyProfileFragment.newInstance(bundle)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.frag_container, frag, MyProfileFragment.TAG)
            transaction.commit()
        }
    }

}
package com.portalpirates.cufit.ui.user.profile

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.portalpirates.cufit.FitActivity
import com.portalpirates.cufit.R


class UserActivity : FitActivity() {

    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var toolbar: Toolbar

    private lateinit var fragContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = null
        postponeEnterTransition()
        setContentView(R.layout.main_layout)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        collapsingToolbar.title = resources.getString(R.string.my_profile)
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.bold_body_text)
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.text_primary))
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.header1)
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.text_primary))


        fragContainer = findViewById(R.id.frag_container)

        appBar = findViewById(R.id.app_bar)

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
package com.portalpirates.cufit.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.portalpirates.cufit.FitActivity
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.progress.ProgressActivity
import com.portalpirates.cufit.ui.user.profile.MyProfileFragment
import com.portalpirates.cufit.ui.user.profile.UserActivity

class HomeActivity : FitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = null
        postponeEnterTransition()
        setContentView(R.layout.main_layout)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        collapsingToolbar.title = resources.getString(R.string.my_home)
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

        bottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.profile -> {
                    val intent = Intent(this@HomeActivity, UserActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.progress -> {
                    val intent = Intent(this@HomeActivity, ProgressActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}
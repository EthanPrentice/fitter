package com.portalpirates.cufit.ui.nav

import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.FitActivity
import com.portalpirates.cufit.ui.view.LockableNestedScrollView


class NavActivity : FitActivity() {

    private var bottomNavigationView: BottomNavigationView? = null
    private var viewPager: FitViewPager? = null

    private var viewPagerAdapter: ViewPagerAdapter? = null

    private var fragScrollView: LockableNestedScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager
        )
        viewPager = findViewById(R.id.view_pager)
        viewPager?.adapter = viewPagerAdapter

        fragScrollView = findViewById(R.id.frag_scrollview)

        bottomNavigationView = findViewById(R.id.bottom_nav)

        initBottomNav()

        fragScrollView?.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    fragScrollView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    viewPager?.minimumHeight = fragScrollView?.measuredHeight ?: 0
                }
            })
    }

    /**
     * Defaults to setting the bottom navigation to Home, User, Progress
     */
    private fun initBottomNav() {
        bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
            viewPager?.currentItem = when (item.itemId) {
                R.id.profile -> 0
                R.id.home -> 1
                R.id.progress -> 2
                else -> 1
            }
            true
        }

        viewPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView?.menu?.findItem(R.id.profile)?.isChecked = true
                    1 -> bottomNavigationView?.menu?.findItem(R.id.home)?.isChecked = true
                    2 -> bottomNavigationView?.menu?.findItem(R.id.progress)?.isChecked = true
                }
                fragScrollView?.scrollY = 0
                appBar?.setExpanded(true)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun hasNavBar(): Boolean {
        return true
    }
}
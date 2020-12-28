package com.ethanprentice.fitter.ui.nav

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ethanprentice.fitter.ui.FitFragment
import com.ethanprentice.fitter.ui.home.HomeFragment
import com.ethanprentice.fitter.ui.progress.ProgressFragment
import com.ethanprentice.fitter.ui.user.profile.MyProfileFragment


class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var currFrag: FitFragment? = null
        private set

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        if (currFrag != obj) {
            currFrag = obj as FitFragment
        }
        super.setPrimaryItem(container, position, obj)
    }

    override fun getItem(position: Int): FitFragment {
        return when (position) {
            0 -> MyProfileFragment.newInstance()
            1 -> HomeFragment.newInstance()
            2 -> ProgressFragment.newInstance()
            else -> throw IllegalStateException()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
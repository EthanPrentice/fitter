package com.portalpirates.cufit.ui.user.profile.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.portalpirates.cufit.R
import com.portalpirates.cufit.ui.user.profile.view.subview.StatBarItem
import com.portalpirates.cufit.ui.view.FitButton
import com.portalpirates.cufit.ui.view.FitCardView

class MyProfileCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val profileImageView: ImageView
    val profileNameView: TextView
    val profileStats: LinearLayout

    val settingsBtn: FitButton

    init {
        val view = inflate(context, R.layout.my_profile_card, this)

        profileImageView = view.findViewById(R.id.profile_img)
        profileNameView = view.findViewById(R.id.profile_name)
        profileStats = view.findViewById(R.id.profile_stats)

        settingsBtn = view.findViewById(R.id.settings_btn)
    }

    fun getStatBarItems(): List<StatBarItem> {
        val lst = ArrayList<StatBarItem>()
        for (i in 0 until profileStats.childCount) {
            lst.add(profileStats.getChildAt(i) as StatBarItem)
        }
        return lst
    }

}
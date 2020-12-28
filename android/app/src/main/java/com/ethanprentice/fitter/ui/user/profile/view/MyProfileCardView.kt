package com.ethanprentice.fitter.ui.user.profile.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.user.FitUser
import com.ethanprentice.fitter.ui.user.profile.view.subview.StatBarItem
import com.ethanprentice.fitter.ui.view.FitButton
import com.ethanprentice.fitter.ui.view.FitCardView

class MyProfileCardView(context: Context, attrs: AttributeSet?, defStyle: Int) : FitCardView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val profileImageView: ImageView
    val profileNameView: TextView

    val profileStats: LinearLayout
    val workoutsStat: StatBarItem
    val currentWeightStat: StatBarItem
    val goalWeightStat: StatBarItem

    val settingsBtn: FitButton

    init {
        val view = inflate(context, R.layout.my_profile_card, null)
        setContentView(view)

        profileImageView = view.findViewById(R.id.profile_img)
        profileNameView = view.findViewById(R.id.profile_name)

        profileStats = view.findViewById(R.id.profile_stats)
        workoutsStat = profileStats.findViewById(R.id.workouts_stat)
        currentWeightStat = profileStats.findViewById(R.id.current_weight_stat)
        goalWeightStat = profileStats.findViewById(R.id.goal_weight_stat)

        settingsBtn = view.findViewById(R.id.settings_btn)
    }

    fun setUser(user: FitUser) {
        profileNameView.text = user.fullName
        workoutsStat.value = "0" // Not implemented yet

        if (user.imageBmp == null) {
            profileImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_avatar))
        } else {
            profileImageView.setImageBitmap(user.imageBmp)
        }

        // TODO: implement MeasuredTextView and use those in the StatBarItem
        currentWeightStat.setValue(user.currentWeight?.number ?: 0)
        goalWeightStat.setValue(user.weightGoal?.number ?: 0)
    }

    fun getStatBarItems(): List<StatBarItem> {
        val lst = ArrayList<StatBarItem>()
        for (i in 0 until profileStats.childCount) {
            lst.add(profileStats.getChildAt(i) as StatBarItem)
        }
        return lst
    }

}
package com.ethanprentice.fitter.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.ethanprentice.fitter.R

/**
 * Base Activity class for CU Fit - All other Activities should inherit from this class
 */
abstract class FitActivity : AppCompatActivity() {

    protected lateinit var fragContainer: FrameLayout

    var appBar: AppBarLayout? = null
        protected set
    var collapsingToolbar: CollapsingToolbarLayout? = null
        protected set
    var toolbar: Toolbar? = null
        protected set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate called for ${javaClass.name}")
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        appBar = findViewById(R.id.app_bar)
        toolbar = findViewById(R.id.toolbar)
        collapsingToolbar = findViewById(R.id.collapsing_toolbar)

        initToolbar()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume called for ${javaClass.name}")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause called for ${javaClass.name}")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop called for ${javaClass.name}")
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy called for ${javaClass.name}")
        super.onDestroy()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun setToolbarTitle(resId: Int) {
        collapsingToolbar?.title = resources.getString(resId)
    }

    fun setToolbarTitle(str: String) {
        collapsingToolbar?.title = str
    }

    fun hasAppBar(): Boolean {
        return appBar != null
    }

    fun hasToolbar(): Boolean {
        return toolbar != null
    }

    open fun hasNavBar(): Boolean {
        return false
    }

    protected open fun initToolbar() {
        setSupportActionBar(toolbar)

        collapsingToolbar?.title = resources.getString(R.string.my_home)
        collapsingToolbar?.setCollapsedTitleTextAppearance(R.style.bold_body_text)
        collapsingToolbar?.setCollapsedTitleTextColor(ContextCompat.getColor(this,
            R.color.text_primary
        ))
        collapsingToolbar?.setExpandedTitleTextAppearance(R.style.header1)
        collapsingToolbar?.setExpandedTitleColor(ContextCompat.getColor(this, R.color.text_primary))
    }

    companion object {
        private const val TAG = "FitActivity"

        // Bundle args
        const val HAS_ACTIVITY_SHARED_ELEM_TRANSITION = "com.ethanprentice.fitter.ui.FitActivity:hasActivitySharedElemTransition"

        // Result codes
        const val REQUEST_LOAD_IMAGE = 0
        const val RESULT_LOAD_IMAGE = 1

        const val ACTIVITY_TRANSITION_MS = 300L
    }

}
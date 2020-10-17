package com.portalpirates.cufit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Base Activity class for CU Fit - All other Activities should inherit from this class
 */
abstract class FitActivity : AppCompatActivity() {

    var appBar: AppBarLayout? = null
        protected set

    protected lateinit var collapsingToolbar: CollapsingToolbarLayout
    protected lateinit var toolbar: Toolbar
    protected lateinit var fragContainer: FrameLayout
    protected lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate called for ${javaClass.name}")
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        appBar = findViewById(R.id.app_bar)
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

    fun hasAppBar(): Boolean {
        return appBar != null
    }

    companion object {
        private const val TAG = "FitActivity"

        // Bundle args
        const val HAS_ACTIVITY_SHARED_ELEM_TRANSITION = "com.portalpirates.cufit.ui.FitActivity:hasActivitySharedElemTransition"

        // Result codes
        const val REQUEST_LOAD_IMAGE = 0
        const val RESULT_LOAD_IMAGE = 1

        const val ACTIVITY_TRANSITION_MS = 300L
    }

}
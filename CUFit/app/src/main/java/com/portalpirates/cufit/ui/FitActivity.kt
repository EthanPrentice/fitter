package com.portalpirates.cufit

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Base Activity class for CU Fit - All other Activities should inherit from this class
 */
abstract class FitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i(TAG, "onCreate called for ${javaClass.name}")
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

    companion object {
        private const val TAG = "FitActivity"

        const val HAS_ACTIVITY_SHARED_ELEM_TRANSITION = "com.portalpirates.cufit.ui.FitFragment:hasActivitySharedElemTransition"
    }

}
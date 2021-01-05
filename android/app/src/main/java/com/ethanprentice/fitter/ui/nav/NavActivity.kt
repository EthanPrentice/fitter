package com.ethanprentice.fitter.ui.nav

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.adt.TaskListener
import com.ethanprentice.fitter.datamodel.data.workout.Workout
import com.ethanprentice.fitter.ui.FitActivity
import com.ethanprentice.fitter.ui.FitApplication
import com.ethanprentice.fitter.ui.home.HomeFragment
import com.ethanprentice.fitter.viewmodel.HomeViewModel
import com.ethanprentice.fitter.ui.view.LockableNestedScrollView
import java.io.IOException


class NavActivity : FitActivity() {

    private val model: HomeViewModel by viewModels()

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

        runWorkoutQueries()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val imageSelectorOwner = model.imageSelectorLock.owner
            model.imageSelectorLock.unlock()

            val selectedImage: Uri = data.data ?: return
            try {
                getBitmapFromUri(selectedImage)?.let { bmp ->
                    imageSelectorOwner?.onSelected(bmp)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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


    fun runWorkoutQueries() {
        queryMyWorkouts()
        queryRecentWorkouts()
    }

    /* View model methods */
    private fun queryMyWorkouts() {
        val userUid = model.firebaseUser!!.uid
        model.getWorkoutsByOwner(userUid, object :
            TaskListener<List<Workout>> {
            override fun onSuccess(value: List<Workout>) {
                model.clearOwnedWorkouts()
                model.addOwnedWorkouts(*(value.toTypedArray()))
            }

            override fun onFailure(e: Exception?) {
                Log.e(HomeFragment.TAG, e?.message.toString())
            }
        })
    }

    private fun queryRecentWorkouts() {
        val userUid = model.firebaseUser!!.uid
        model.getRecentWorkouts(userUid, object : TaskListener<List<Workout>> {
            override fun onSuccess(value: List<Workout>) {
                model.clearRecentWorkouts()
                model.addRecentWorkouts(*(value.toTypedArray()))
            }

            override fun onFailure(e: Exception?) {
                Log.e(HomeFragment.TAG, e?.message.toString())
            }
        })
    }


    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()

        return image
    }
}
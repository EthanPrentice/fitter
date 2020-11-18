package com.portalpirates.cufit.ui.user.welcome

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.portalpirates.cufit.ui.FitActivity
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.adt.TaskListener
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.nav.NavActivity
import java.io.IOException


class WelcomeActivity : FitActivity(), WelcomeFragment.WelcomeFragListener {

    private val model: WelcomeViewModel by viewModels()

    init {
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = null

        postponeEnterTransition()
        setContentView(R.layout.frag_only_layout)

        fragContainer = findViewById(R.id.frag_container)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putBoolean(HAS_ACTIVITY_SHARED_ELEM_TRANSITION, true)

            val frag = WelcomeIntroFragment.newInstance(bundle)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.frag_container, frag, WelcomeIntroFragment.TAG)
            transaction.commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage: Uri = data.data ?: return

            try {
                getBitmapFromUri(selectedImage)?.let { bmp ->
                    model.setUserImage(bmp)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun userReadyToBuild(builder: FitUserBuilder) {
        FitApplication.instance.userManager.managementReceiver.createFireStoreUser(builder, object :
            TaskListener<Unit?> {
            override fun onSuccess(value: Unit?) {
                Toast.makeText(FitApplication.instance.applicationContext, "Account creation successful!\nWelcome ${builder.firstName}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@WelcomeActivity, NavActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(e: Exception?) {
                Log.e(TAG, e.toString())
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


    companion object {
        const val TAG = "WelcomeActivity"
    }

}
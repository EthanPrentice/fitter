package com.portalpirates.cufit.ui.user.welcome

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    private val _userImage = MutableLiveData<Bitmap?>().apply {
        value = null
    }
    val userImage: LiveData<Bitmap?> = _userImage

    fun setUserImage(bmp: Bitmap?) {
        _userImage.postValue(bmp)
    }


    private val _userSex = MutableLiveData<String?>().apply {
        value = null
    }
    val userSex: LiveData<String?> = _userSex

    fun setUserSex(string: String?) {
        _userSex.postValue(string)
    }
}
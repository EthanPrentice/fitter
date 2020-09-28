package com.portalpirates.cufit.ui.user.welcome

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.user.FitUserBuilder
import com.portalpirates.cufit.datamodel.data.user.UserSex
import com.portalpirates.cufit.datamodel.data.measure.Weight
import java.util.*

class WelcomeViewModel : ViewModel() {

    private val _userImage = MutableLiveData<Bitmap?>()
    val userImage: LiveData<Bitmap?> = _userImage

    private val _userFirstName = MutableLiveData<String?>()
    val userFirstName: LiveData<String?> = _userFirstName

    private val _userLastName = MutableLiveData<String?>()
    val userLastName: LiveData<String?> = _userLastName

    private val _userBirthDate = MutableLiveData<Date?>()
    val userBirthDate: LiveData<Date?> = _userBirthDate

    private val _userSex = MutableLiveData<UserSex>().apply {
        value = UserSex.MALE
    }
    val userSex: LiveData<UserSex> = _userSex

    private val _userCurrentWeight = MutableLiveData<Weight?>()
    val userCurrentWeight: LiveData<Weight?> = _userCurrentWeight

    private val _userCurrentHeight = MutableLiveData<Height?>()
    val userCurrentHeight: LiveData<Height?> = _userCurrentHeight

    private val _userWeightGoal = MutableLiveData<Weight?>()
    val userWeightGoal: LiveData<Weight?> = _userWeightGoal

    fun setUserImage(bmp: Bitmap?) {
        _userImage.postValue(bmp)
    }

    fun setUserFirstName(firstName: String) {
        _userFirstName.postValue(firstName)
    }

    fun setUserLastName(lastName: String) {
        _userLastName.postValue(lastName)
    }

    fun setUserBirthDate(birthDate: Date) {
        _userBirthDate.postValue(birthDate)
    }

    fun setUserSex(sex: String?) {
        val userSex = UserSex.getFromString(sex)
        if (userSex == null) {
            Log.e(TAG, "String was not parsable")
            return
        }
        _userSex.postValue(userSex)
    }

    fun setUserCurrentWeight(currentWeight: Weight?) {
        _userCurrentWeight.postValue(currentWeight)
    }

    fun setUserCurrentHeight(currentHeight: Height?) {
        _userCurrentHeight.postValue(currentHeight)
    }

    fun setUserWeightGoal(weightGoal: Weight?) {
        _userWeightGoal.postValue(weightGoal)
    }

    @Throws(FitUserBuilder.UserBuildException::class)
    fun getBuilder(): FitUserBuilder {
        fun getException(): FitUserBuilder.UserBuildException {
            return FitUserBuilder.UserBuildException("All required fields have not been provided for user! Cannot build!")
        }

        return FitUserBuilder().setImage(userImage.value)
            .setFirstName(userFirstName.value ?: throw getException())
            .setLastName(userLastName.value ?: throw getException())
            .setBirthDate(userBirthDate.value ?: throw getException())
            .setSex(userSex.value ?: throw getException())
            .setCurrentWeight(userCurrentWeight.value)
            .setCurrentHeight(userCurrentHeight.value)
            .setWeightGoal(userWeightGoal.value)
    }

    companion object {
        const val TAG = "WelcomeViewModel"
    }
}
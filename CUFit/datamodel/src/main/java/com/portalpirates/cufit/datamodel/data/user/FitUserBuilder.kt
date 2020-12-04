package com.portalpirates.cufit.datamodel.data.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.Blob
import com.portalpirates.cufit.datamodel.FitException
import com.portalpirates.cufit.datamodel.data.measure.Height
import com.portalpirates.cufit.datamodel.data.measure.Weight
import com.portalpirates.cufit.datamodel.util.ImageUtil
import java.io.ByteArrayOutputStream
import java.util.*


class FitUserBuilder {

    // required
    var birthDate: Date? = null
    var firstName: String? = null
    var lastName: String? = null
    var sex: UserSex? = null

    // optional
    var uid: String? = null
    var imageBmp: Bitmap? = null
    var currentWeight: Weight? = null
    var currentHeight: Height? = null
    var weightGoal: Weight? = null

    @Throws(UserBuildException::class)
    fun build(): FitUser {
        if (hasRequiredInputs()) {
            return FitUser(birthDate!!, firstName!!, lastName!!, currentWeight, currentHeight, weightGoal, imageBmp)
        } else {
            throw UserBuildException("All required fields have not been provided for user! Cannot build!")
        }
    }

    fun setUid(uid: String?): FitUserBuilder {
        this.uid = uid
        return this
    }

    fun setCurrentWeight(weight: Weight?) : FitUserBuilder {
        currentWeight = weight
        return this
    }

    fun setCurrentHeight(height: Height?) : FitUserBuilder {
        currentHeight = height
        return this
    }

    fun setWeightGoal(weight: Weight?) : FitUserBuilder {
        weightGoal = weight
        return this
    }

    fun setBirthDate(date: Date): FitUserBuilder {
        birthDate = date
        return this
    }

    fun setFirstName(name: String): FitUserBuilder {
        firstName = name
        return this
    }

    fun setLastName(name: String): FitUserBuilder {
        lastName = name
        return this
    }

    fun setSex(sex: UserSex): FitUserBuilder {
        this.sex = sex
        return this
    }

    fun setImage(bmp: Bitmap?): FitUserBuilder {
        imageBmp = bmp
        return this
    }

    fun setImageBlob(byteArr: ByteArray?): FitUserBuilder {
        if (byteArr == null) {
            imageBmp = null
            return this
        }

        val options = BitmapFactory.Options().apply {
            inMutable = true
        }
        imageBmp = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.size, options)
        return this
    }

    fun hasRequiredInputs(): Boolean {
        return birthDate != null &&
                firstName != null &&
                lastName != null &&
                sex != null
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        val imageBlob = imageBmp?.let { bmp ->
            val bos = ByteArrayOutputStream()
            ImageUtil.getResizedBitmap(bmp, ImageUtil.MAX_BMP_SIZE).compress(Bitmap.CompressFormat.PNG, 100, bos)
            Blob.fromBytes(bos.toByteArray())
        }

        val hashMap = hashMapOf<String, Any?>(
            UserField.BIRTH_DATE.toString() to birthDate,
            UserField.FIRST_NAME.toString() to firstName,
            UserField.LAST_NAME.toString() to lastName,
            UserField.SEX.toString() to sex!!.char.toString(),
            UserField.IMAGE_BMP.toString() to imageBlob
            // TODO: previous weights
            // TODO: previous heights
        )

        currentWeight?.addFieldsToHashMap(hashMap, UserField.CURRENT_WEIGHT.toString())
        currentHeight?.addFieldsToHashMap(hashMap,  UserField.CURRENT_HEIGHT.toString())
        weightGoal?.addFieldsToHashMap(hashMap,  UserField.WEIGHT_GOAL.toString())

        return hashMap
    }

    class UserBuildException(s: String, cause: Throwable?) : FitException(s, cause) {
        constructor(s: String) : this(s, null)
    }

}
package com.portalpirates.cufit.datamodel.data.user

import com.portalpirates.cufit.datamodel.cloud.CloudInterface
import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface

enum class UserField(val fieldName: String) {
    UID(CloudInterface.UID),
    BIRTH_DATE(UserCloudInterface.BIRTH_DATE),
    FIRST_NAME(UserCloudInterface.FIRST_NAME),
    LAST_NAME(UserCloudInterface.LAST_NAME),
    SEX(UserCloudInterface.SEX),
    CURRENT_WEIGHT(UserCloudInterface.CURRENT_WEIGHT),
    WEIGHT_GOAL(UserCloudInterface.WEIGHT_GOAL);

    override fun toString(): String {
        return fieldName
    }

}
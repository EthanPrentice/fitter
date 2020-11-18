package com.portalpirates.cufit.datamodel.data.user

enum class UserField(val fieldName: String) {
    UID("uid"),
    IMAGE_BMP("avatar_blob"),
    BIRTH_DATE("birth_date"),
    FIRST_NAME("name.first"),
    LAST_NAME("name.last"),
    CURRENT_WEIGHT("current_weight"),
    CURRENT_HEIGHT("current_height"),
    WEIGHT_GOAL("weight_goal"),
    SEX("gender");

    override fun toString(): String {
        return fieldName
    }
}
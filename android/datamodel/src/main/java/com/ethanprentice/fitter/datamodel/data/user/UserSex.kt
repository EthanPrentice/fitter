package com.ethanprentice.fitter.datamodel.data.user

enum class UserSex(val formalName: String, val secondaryName: String, val char: Char) {
    MALE("Male", "Man", 'M'),
    FEMALE("Female", "Woman", 'F'),
    OTHER("Other", "Other", 'X');

    companion object {
        fun getFromString(str: String?): UserSex? {
            str ?: return null

            return when (str.toLowerCase()) {
                "man", "male", "m" -> MALE
                "woman", "female", "f" -> FEMALE
                "other", "x" -> OTHER
                else -> null
            }
        }
    }
}
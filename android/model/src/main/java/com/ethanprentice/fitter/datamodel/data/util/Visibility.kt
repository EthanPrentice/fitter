package com.ethanprentice.fitter.datamodel.data.util

enum class Visibility(val str: String) {
    PUBLIC("public"),
    PRIVATE("private");


    override fun toString(): String {
        return str
    }

    companion object {
        fun getFromString(str: String?): Visibility? {
            str ?: return null

            for (value in values()) {
                if (str == value.str) {
                    return value
                }
            }
            return null
        }
    }
}
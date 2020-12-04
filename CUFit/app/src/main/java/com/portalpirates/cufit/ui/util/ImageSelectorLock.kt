package com.portalpirates.cufit.ui.util

class ImageSelectorLock {
    var owner: ImageSelector? = null
        private set

    private var locked = false

    fun lock(v: ImageSelector) {
        locked = true
        owner = v
    }

    fun unlock() {
        owner = null
        locked = false
    }

    fun isLocked() = locked
}
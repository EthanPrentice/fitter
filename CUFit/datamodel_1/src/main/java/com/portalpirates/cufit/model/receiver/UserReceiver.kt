package com.portalpirates.cufit.model.receiver

class UserReceiver {

    companion object {
        private var actualInstance: UserReceiver? = null
        val instance: UserReceiver
            get() {
                if (actualInstance == null) {
                    actualInstance = UserReceiver()
                }
                return actualInstance!!
            }
    }
}
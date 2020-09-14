package com.portalpirates.cufit.model.processing

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.portalpirates.cufit.model.cloud.CloudInterface
import com.portalpirates.cufit.model.provider.Provider

abstract class DataProcessor (
    protected var cloudInterface: CloudInterface,
    protected var provider: Provider) {

    init {
        provider.setDataProcessor(this)
    }

    /**
     * Runs [func] with the [cloudInterface]'s firestore instance as a parameter
     */
    protected fun processAndSendToFirestore(func: (firestore: FirebaseFirestore) -> Unit) {
        func(cloudInterface.db)
    }

    protected fun processAndSendToAuth(func: (auth: FirebaseAuth) -> Unit) {
        func(cloudInterface.auth)
    }

    protected fun processAndUpdateProvider(func: (provider: Provider) -> Unit) {
        func(provider)
    }
}
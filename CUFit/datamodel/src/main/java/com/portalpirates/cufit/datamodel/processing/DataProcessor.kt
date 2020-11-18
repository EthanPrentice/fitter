package com.portalpirates.cufit.datamodel.processing

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.portalpirates.cufit.datamodel.cloud.CloudInterface
import com.portalpirates.cufit.datamodel.manager.Manager
import com.portalpirates.cufit.datamodel.provider.Provider

internal abstract class DataProcessor(protected val manager: Manager) {

    protected val provider: Provider
        get() = manager.provider

    protected val cloudInterface: CloudInterface
        get() = manager.cloudInterface

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
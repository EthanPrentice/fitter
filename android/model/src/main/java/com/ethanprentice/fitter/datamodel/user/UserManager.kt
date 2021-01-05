package com.ethanprentice.fitter.datamodel.user

import com.ethanprentice.fitter.datamodel.adt.Manager
import com.ethanprentice.fitter.datamodel.user.cloud.UserAuthCloudInterface
import com.ethanprentice.fitter.datamodel.user.cloud.UserManagementCloudInterface
import com.ethanprentice.fitter.datamodel.user.cloud.UserQueryCloudInterface
import com.ethanprentice.fitter.datamodel.user.processing.UserAuthDataProcessor
import com.ethanprentice.fitter.datamodel.user.processing.UserManagementDataProcessor
import com.ethanprentice.fitter.datamodel.user.processing.UserQueryDataProcessor
import com.ethanprentice.fitter.datamodel.user.provider.UserProvider
import com.ethanprentice.fitter.datamodel.user.receiver.UserAuthReceiver
import com.ethanprentice.fitter.datamodel.user.receiver.UserManagementReceiver

internal class UserManager private constructor() : Manager() {

    init {
        cloudInterfaces[QUERY] = UserQueryCloudInterface(this)
        dataProcessors[QUERY] = UserQueryDataProcessor(this)
        providers[QUERY] = UserProvider(this)

        cloudInterfaces[MANAGEMENT] = UserManagementCloudInterface(this)
        dataProcessors[MANAGEMENT] = UserManagementDataProcessor(this)
        receivers[MANAGEMENT] = UserManagementReceiver(this)

        cloudInterfaces[AUTH] = UserAuthCloudInterface(this)
        dataProcessors[AUTH] = UserAuthDataProcessor(this)
        receivers[AUTH] = UserAuthReceiver(this)
    }

    /* USER QUERYING */
    internal val queryCloudInterface get() = cloudInterfaces[QUERY] as UserQueryCloudInterface
    internal val queryDataProcessor  get() = dataProcessors[QUERY]  as UserQueryDataProcessor
    val provider                     get() = providers[QUERY]       as UserProvider

    /* USER MANAGEMENT */
    internal val managementCloudInterface get() = cloudInterfaces[MANAGEMENT] as UserManagementCloudInterface
    internal val managementDataProcessor  get() = dataProcessors[MANAGEMENT]  as UserManagementDataProcessor
    val managementReceiver                get() = receivers[MANAGEMENT]       as UserManagementReceiver

    /* USER AUTHENTICATION */
    internal val authCloudInterface get() = cloudInterfaces[AUTH] as UserAuthCloudInterface
    internal val authDataProcessor  get() = dataProcessors[AUTH]  as UserAuthDataProcessor
    val authReceiver                get() = receivers[AUTH]       as UserAuthReceiver


    companion object {
        /* Subgroups */
        private const val QUERY = 0
        private const val MANAGEMENT = 1
        private const val AUTH = 2

        private var instance: UserManager? = null
        fun getInstance(): UserManager {
            if (instance == null) {
                instance = UserManager()
            }
            return instance!!
        }
    }
}
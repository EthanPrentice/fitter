package com.portalpirates.cufit.datamodel.user

import com.portalpirates.cufit.datamodel.adt.Manager
import com.portalpirates.cufit.datamodel.user.cloud.UserAuthCloudInterface
import com.portalpirates.cufit.datamodel.user.cloud.UserManagementCloudInterface
import com.portalpirates.cufit.datamodel.user.cloud.UserQueryCloudInterface
import com.portalpirates.cufit.datamodel.user.processing.UserAuthDataProcessor
import com.portalpirates.cufit.datamodel.user.processing.UserManagementDataProcessor
import com.portalpirates.cufit.datamodel.user.processing.UserQueryDataProcessor
import com.portalpirates.cufit.datamodel.user.provider.UserProvider
import com.portalpirates.cufit.datamodel.user.receiver.UserAuthReceiver
import com.portalpirates.cufit.datamodel.user.receiver.UserManagementReceiver

class UserManager : Manager() {

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
    }
}
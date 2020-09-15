package com.portalpirates.cufit.datamodel.manager

import com.portalpirates.cufit.datamodel.cloud.UserCloudInterface
import com.portalpirates.cufit.datamodel.processing.DataProcessor
import com.portalpirates.cufit.datamodel.processing.UserDataProcessor
import com.portalpirates.cufit.datamodel.provider.UserProvider
import com.portalpirates.cufit.datamodel.receiver.UserReceiver

class UserManager : Manager() {
    override val cloudInterface: UserCloudInterface = UserCloudInterface(this)
    override val dataProcessor: DataProcessor = UserDataProcessor(this)
    override val provider: UserProvider = UserProvider(this)
    override val receiver: UserReceiver = UserReceiver(this)
}
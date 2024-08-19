package org.luteh.ecommerce

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isIos: Boolean
        get() = true
}

actual fun getPlatform(): Platform = IOSPlatform()
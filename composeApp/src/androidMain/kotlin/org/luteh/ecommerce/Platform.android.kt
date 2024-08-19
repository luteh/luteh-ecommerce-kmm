package org.luteh.ecommerce

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isIos: Boolean
        get() = false
}

actual fun getPlatform(): Platform = AndroidPlatform()
package org.luteh.ecommerce

import android.os.Build
import org.luteh.ecommerce.domain.annotation.ExcludeFromCoverage

@ExcludeFromCoverage
class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isIos: Boolean
        get() = false
}

@ExcludeFromCoverage actual fun getPlatform(): Platform = AndroidPlatform()

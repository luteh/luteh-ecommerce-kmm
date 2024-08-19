package org.luteh.ecommerce

interface Platform {
    val name: String
    val isIos: Boolean
}

expect fun getPlatform(): Platform
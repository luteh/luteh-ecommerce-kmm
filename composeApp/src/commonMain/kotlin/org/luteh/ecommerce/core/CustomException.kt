package org.luteh.ecommerce.core

class CustomException : Exception {
    // Default constructor
    constructor() : super()

    // Constructor with message
    constructor(message: String?) : super(message)

    // Constructor with message and cause
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    // Constructor with cause
    constructor(cause: Throwable?) : super(cause)
}
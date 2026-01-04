package org.luteh.ecommerce.domain.model

sealed interface SessionResult {
    data object LoggedIn : SessionResult

    data object NotLoggedIn : SessionResult
}

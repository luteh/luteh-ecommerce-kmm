package org.luteh.ecommerce.presentation.core

sealed interface ResultState<out R> {
    data class Success<out T>(val data: T) : ResultState<T>
    data class Error(val exception: Exception) : ResultState<Nothing>
    data object Loading : ResultState<Nothing>
    data object Idle : ResultState<Nothing>

    fun getOrNull(): R? =
        when (this) {
            is Success -> this.data
            else -> null
        }
}
package org.luteh.ecommerce.domain.model

data class RegisterParam(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val roleId: String
)

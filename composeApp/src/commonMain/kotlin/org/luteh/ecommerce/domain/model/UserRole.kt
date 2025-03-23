package org.luteh.ecommerce.domain.model

data class UserRole(val id: String, val name: String) {
    companion object {
        val dummies
            get() =
                listOf(
                    UserRole(id = "1", name = "Admin"),
                    UserRole(id = "2", name = "User"),
                    UserRole(id = "3", name = "Guest"),
                )
    }
}

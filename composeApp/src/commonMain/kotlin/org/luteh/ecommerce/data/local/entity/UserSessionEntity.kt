package org.luteh.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_session")
data class UserSessionEntity(
    @PrimaryKey
    val id: Int = 1, // Single session
    val accessToken: String,
    val expirationTimestamp: Long,
    val userId: String,
    val email: String,
    val name: String
)


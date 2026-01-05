package org.luteh.ecommerce.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.luteh.ecommerce.data.local.entity.UserSessionEntity

@Dao
interface UserSessionDao {
    @Query("SELECT * FROM user_session WHERE id = 1")
    fun observeUserSession(): Flow<UserSessionEntity?>

    @Query("SELECT * FROM user_session WHERE id = 1")
    suspend fun getUserSession(): UserSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSession(session: UserSessionEntity)

    @Query("DELETE FROM user_session WHERE id = 1") suspend fun clearUserSession()
}

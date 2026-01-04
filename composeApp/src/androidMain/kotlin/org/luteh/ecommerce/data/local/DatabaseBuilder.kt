package org.luteh.ecommerce.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.luteh.ecommerce.data.local.database.AppDatabase
import org.luteh.ecommerce.data.local.database.MIGRATION_1_2
import org.luteh.ecommerce.data.local.database.MIGRATION_2_3

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath("luteh_ecommerce.db")
    return Room.databaseBuilder<AppDatabase>(context = context, name = dbFile.absolutePath)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
}

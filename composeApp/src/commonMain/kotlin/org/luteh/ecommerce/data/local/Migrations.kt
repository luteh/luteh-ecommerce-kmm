package org.luteh.ecommerce.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `shipping_address` (`id` INTEGER NOT NULL, `fullName` TEXT NOT NULL, `addressLine` TEXT NOT NULL, `city` TEXT NOT NULL, `postalCode` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_session` (`id` INTEGER NOT NULL, `accessToken` TEXT NOT NULL, `expirationTimestamp` INTEGER NOT NULL, `userId` TEXT NOT NULL, `email` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}


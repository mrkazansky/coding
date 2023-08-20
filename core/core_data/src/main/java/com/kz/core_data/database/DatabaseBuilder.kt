package com.kz.core_data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseBuilder {
    fun <T : RoomDatabase> createModule(context: Context, name: String, database: Class<T>): T {
        return Room.databaseBuilder(context, database, name).build()
    }
}
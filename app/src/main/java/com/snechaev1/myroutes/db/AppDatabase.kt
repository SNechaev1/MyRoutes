package com.snechaev1.myroutes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snechaev1.myroutes.data.Route

@Database(entities = [Route::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
}

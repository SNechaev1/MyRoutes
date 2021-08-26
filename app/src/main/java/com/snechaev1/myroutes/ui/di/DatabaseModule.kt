package com.snechaev1.myroutes.ui.di

import android.content.Context
import androidx.room.Room
import com.snechaev1.myroutes.db.AppDatabase
import com.snechaev1.myroutes.db.RouteDao
import com.snechaev1.myroutes.utils.GlobalValues
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    @Db
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            GlobalValues.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRouteDao(@Db database: AppDatabase): RouteDao {
        return database.routeDao()
    }

    @Provides
    @Singleton
    @DbInMemory
    fun provideInMemoryDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

}

package com.snechaev1.myroutes.db

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.room.*
import com.snechaev1.myroutes.data.Route

@Dao
interface RouteDao {

    @Query("SELECT * FROM routes ORDER BY created DESC")
    fun getAllRoutes(): PagingSource<Int, Route>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoutes(routes: List<Route>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(vararg route: Route)

    @Delete
    suspend fun deleteRoute(route: Route)

    @Query("DELETE FROM routes")
    fun dropRoutesTable()

    @Query("SELECT * FROM routes ORDER BY created DESC")
    fun selectAllRoutesCursor(): Cursor

    @Query("SELECT * FROM routes WHERE created = :id")
    fun selectRoutesById(id: Long): Cursor?

//    @Transaction
//    suspend fun insertAndGetTransaction(routes: List<Route>): List<Route> {
//        insertAllRoutes(routes)
//        return getAllRoutes()
//    }
}

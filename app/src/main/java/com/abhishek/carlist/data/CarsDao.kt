package com.abhishek.carlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarsDao {

    // Query to fetch all the data from the
    // SQLite database
    // No need of suspend method here
    @Query("SELECT * FROM cars")
    // Kotlin flow is an asynchronous stream of values
    fun getAllCars() : Flow<List<CarList>>


    // If a new data is inserted with same primary key
    // It will get replaced by the previous one
    // This ensures that there is always a latest
    // data in the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // The fetching of data should NOT be done on the
    // Main thread. Hence coroutine is used
    // If it is executing on one one thread, it may suspend
    // its execution there, and resume in another one
    suspend fun insertCars(cars : List<CarList>)

    // Once the device comes online, the cached data
    // need to be replaced, i.e. delete it
    // Again it will use coroutine to achieve this task
    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()
}
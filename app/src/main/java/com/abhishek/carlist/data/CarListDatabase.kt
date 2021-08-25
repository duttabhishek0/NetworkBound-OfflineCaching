package com.abhishek.carlist.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(entities = [CarList::class], version = 1)
abstract class CarListDatabase : RoomDatabase() {
    abstract fun carsDao(): CarsDao
}

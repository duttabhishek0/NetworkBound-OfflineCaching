package com.abhishek.carlist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data Class to store the data
@Entity(tableName = "cars")
data class CarList (
    @PrimaryKey val name : String,
    val make_and_model : String,
    val color: String,
    val transmission : String,
    val drive_type : String,
    val fuel_type : String,
    val car_type : String
)
package com.abhishek.carlist.data

import androidx.room.withTransaction
import com.abhishek.carlist.api.CarListAPI
import com.abhishek.carlist.util.networkBoundResource
import kotlinx.coroutines.delay
import javax.inject.Inject

class CarListRepository @Inject constructor(
    private val api: CarListAPI,
    private val db: CarListDatabase
) {
    private val carsDao = db.carsDao()

    fun getCars() = networkBoundResource(
        query = {
            carsDao.getAllCars()
        },
        fetch = {
            delay(2000)
            api.getCarList()
        },
        saveFetchResult = { CarList ->
            db.withTransaction {
                carsDao.deleteAllCars()
                carsDao.insertCars(CarList)
            }
        }
    )
}
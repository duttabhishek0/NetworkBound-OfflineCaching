package com.abhishek.carlist.api

import com.abhishek.carlist.data.CarList
import retrofit2.http.GET

interface CarListAPI {
    companion object{
        const val BASE_URL = "https://random-data-api.com/api/"
    }

    // The number of cars can be varied using the size.
    // By default it is kept at 20, but can be tweaked.
    @GET("vehicle/random_vehicle?size=20")
    suspend fun getCarList() : List<CarList>

}
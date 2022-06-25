package com.abhishek.carlist.api

import com.abhishek.carlist.data.CarList
import retrofit2.http.GET

interface CarListAPI {
    companion object{
        const val BASE_URL = "https://random-data-api.com/api/"
    }

    /* Query to get a list of 10 cars */
    @GET("vehicle/random_vehicle?size=10")
    suspend fun getCarList() : List<CarList>

}
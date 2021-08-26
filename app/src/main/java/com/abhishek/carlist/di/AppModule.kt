package com.abhishek.carlist.di

import android.app.Application
import androidx.room.Room
import com.abhishek.carlist.api.CarListAPI
import com.abhishek.carlist.data.CarListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(CarListAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCarListAPI(retrofit: Retrofit): CarListAPI =
        retrofit.create(CarListAPI::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CarListDatabase =
        Room.databaseBuilder(app, CarListDatabase::class.java, "carlist_database")
            .build()
}
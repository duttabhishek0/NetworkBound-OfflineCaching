package com.abhishek.carlist.features.carlist

import androidx.lifecycle.*
import com.abhishek.carlist.api.CarListAPI
import com.abhishek.carlist.data.CarList
import com.abhishek.carlist.data.CarListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarListViewModel @Inject constructor(
    repository : CarListRepository
) : ViewModel() {
    val cars = repository.getCars().asLiveData()
}
package com.abhishek.carlist.features.carlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.carlist.api.CarListAPI
import com.abhishek.carlist.data.CarList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarListViewModel @Inject constructor(
    api : CarListAPI
) : ViewModel() {

    private val carListLiveData =  MutableLiveData<List<CarList>>()
    val carList : LiveData<List<CarList>> = carListLiveData

    init {
        viewModelScope.launch {
            val carList = api.getCarList()
            delay(2000)
            carListLiveData.value = carList
        }
    }
}
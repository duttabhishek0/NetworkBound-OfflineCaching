# NetworkBound-OfflineCaching
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
![Android CI](https://github.com/duttabhishek0/NetworkBound-OfflineCaching//workflows/Android%20CI/badge.svg)
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/github/license/duttabhishek0/NetworkBound-OfflineCaching"/></a>

<br>
An android app to  demonstrate offline caching capabilities offered by JetPack Compose.

The <a href = "https://developer.android.com/jetpack/guide"> Jetpack article</a> describes a way to provide data from a web service or retrieve data from an offline storage(if available). This repository contains the files required to make an application which can demonstrate such capabilities.

Before directly jumping into the code, lets see what is the algorightm.

# The Algorithm
The block diagram represents the control flow :
<br></br>
![NetworkBoundResource](https://user-images.githubusercontent.com/56694152/130604011-73d3ca1d-aee2-4dc2-995f-9e0d7e9a522e.jpg)

The goal here is to minimize the changes in the user-expereince. Important things to be noted:
<ul>
  <li> For the first time, when the application is started, the data is fetched from the webservice(restAPI, AWS, etc) in a background thread.</li>
  <li> As soon as the data fetch is done, all the data is stored in the local database, using the ROOM persistence library</li>
  <li> Parallelly, the data is exposed and shown in the activity/ fragment.</li>
  <li> If any change is there in the data, then the whole view is not refreshed, rather it is fetched in the background thread, laterwards the new data is replaced with the old data</li>
</ul>


The app follows the <a href = "https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjYuM7Xv8nyAhVMAHIKHSOjDr4QFnoECAYQAQ&url=https%3A%2F%2Fdeveloper.android.com%2Ftopic%2Flibraries%2Farchitecture%2Fviewmodel&usg=AOvVaw3f_7HpGuQps9xX6BXFMqhB" > MVVM </a> architecture.

The core part of this application is the `NetworkBoundResource.kt` file, where the magic happens.

##### Code
```kotlin
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}
```

<ol>
  <li>The above code first checks if there is any requirement for fetching the data or not.</li>.
  <ul>
    <li>If it is required to fetch the data, then it is emitted.</li> 
    <li>Else just look into the map</li>
  </ul>
  <li> Kotlin Flow is used here</li>
  </ol>
 <br></br>
  

# Application
This repository contains code for an android application, which basically shows a list of data fetched from a random API generator, using `RetrofitAPI` using which APIs are converted into callable objects. 

The following data are required to be fetched  and shown in the activity.

##### Code

```kotlin
// Data Class to store the data
@Entity(tableName = "cars")
data class CarList (
    @PrimaryKey val make_and_model : String,
    val color: String,
    val transmission : String,
    val drive_type : String,
    val fuel_type : String,
    val car_type : String
)
```

A `repository` will be used. Repository pattern is one of the design patterns that available out there.A Repository is defined as a collection of domain objects that reside in the memory.


`DAO` for the API path

##### Code

```kotlin
interface CarsDao {
    @Query("SELECT * FROM cars")
    fun getAllCars() : Flow<List<CarList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCars(cars : List<CarList>)
    
    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()
}
```

`Repository` class to centralize the data access

##### Code

```kotlin
class CarListRepository @Inject constructor(
    private val api : CarListAPI,
    private val db : CarListDatabase
) {
    private  val carsDao = db.carsDao()

    fun getCars() = networkBoundResource(
        query = {
            carsDao.getAllCars()
        },
        fetch = {
            delay(2000)
            api.getCarList()
        },
        saveFetchResult =  { CarList ->
            db.withTransaction {
                carsDao.deleteAllCars()
                carsDao.insertCars(CarList)
            }
        }
    )
}
```

This thing has to be implemented in a `viewModel` from which data will be exposed on a view or a fragment. 
The view model can get data from the repository by observing it's live data.


##### Code

```kotlin
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
```
Adding a repository between the data source and a view is recommended by Android, as it seperates the view, so that focus can be put seperately on increasing the UI of app and the database. Moreover, the repository helps by centralising the data access, which directly reduces the boilerplate code.

The app uses [MVVM Architecture](https://developer.android.com/jetpack/docs/guide#recommended-app-arch)

## Contributing [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/duttabhishek32/MiniNetworkBound-OfflineCaching/issues) 
<div align="center">STEPS:-</div>
<ol>
<li> Go to https://github.com/duttabhishek32/NetworkBound-OfflineCaching fork  repo.(It's very easy, just follow the steps).</li>
<li> Now click on the "*Issues*"  and see which issues you can work on.</li>
<li> Open the "*database.cpp*" file and then click on the edit button and then do the changes. </li>
<li> Click on the "*Commit changes *", and then click on the "*Create Pull Request*"  button.</li>
<li> I will then reveiw the PR(Pull Request) and if I found it to be correct I will merge it in main. </li>
 </ol>


## Output
The only activity
![Screenshot from 2021-08-24 09-42-19](https://user-images.githubusercontent.com/56694152/130609142-02a3bd38-c567-4424-a720-7c04832b70bd.png)


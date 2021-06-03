package com.dvt.weather.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvt.weather.R
import com.dvt.weather.adaptor.WeatherAdapter
import com.dvt.weather.database.CurrentWeather
import com.dvt.weather.database.Forecast
import com.dvt.weather.database.ForecastDao
import com.dvt.weather.database.RecyclerViewModel
import com.dvt.weather.repository.ForecastViewModel
import com.dvt.weather.restApi.CurrentWeather.CurrentWeatherResponse
import com.dvt.weather.restApi.ForecastViewModelApi
import com.dvt.weather.restApi.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {


    private lateinit var forecastViewModel: ForecastViewModel
    lateinit var viewModel: WeatherViewModel

    lateinit var forecastViewModelApi: ForecastViewModelApi

    //------RoomDao
    lateinit var forecastDao: ForecastDao
    lateinit var currentWeatherResponse: CurrentWeatherResponse

    //Recyclerview
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var recyclerViewModel: RecyclerViewModel

    //global variable of FusedLocationProviderClient
    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var current_lat: Double = 0.0
    var current_long: Double = 0.0
    var currentPlace: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Request permisssion
        RequestLocationPermission()

        //init items
        val textViewCurrentTemp1: TextView = findViewById(R.id.textViewCurrentTemp1)
        val textViewCurrentTemp2: TextView = findViewById(R.id.textViewCurrentTemp2)
        val textViewMinTemmp: TextView = findViewById(R.id.textViewMinTemp)
        val textViewMaxTemp: TextView = findViewById(R.id.textViewMaxTemp)
        val textViewTempDesc: TextView = findViewById(R.id.textViewTempDesc)
        val ImageViewTemp: ImageView = findViewById(R.id.ImageViewTemp)
        val textViewUpdatedAt: TextView = findViewById(R.id.textViewUpdatedAt)
        val textViewCity: TextView = findViewById(R.id.textViewCity)
        val textViewFeel: TextView = findViewById(R.id.textViewFeelLike)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        // initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        weatherAdapter = WeatherAdapter(mutableListOf())

        //init ForecastViewModel Room database
        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        forecastViewModel.getForecast().observe(this, Observer<List<Forecast>> { forecast ->
            weatherAdapter.setListData(forecast)
        })

        recyclerView.adapter = weatherAdapter
        //currentWeather data from Room
        forecastViewModel.getCurrent().observe(this, Observer<List<CurrentWeather>> {
            // assigning elements

            var total_list = it.size
            var i = 0
            var temperatureDescription: String = ""

            while (i < total_list) {
                textViewCity.text = "${it[i].place}"
                textViewCurrentTemp1.text = ("${it[i].temperature_current} ° ")
                textViewCurrentTemp2.text = ("${it[i].temperature_current} ° ")
                textViewMaxTemp.text = ("${it[i].temperature_max} ° ")
                textViewMinTemp.text = ("${it[i].temperature_min} ° ")
                textViewTempDesc.text = (" ${it[i].temperature}")
                textViewFeelLike.text = ("Feels like ${it[i].feelLike}°")
                temperatureDescription = it[i].temperature
                textViewUpdatedAt.text = "updated ${it[i].updatedAt}"
                ImageViewTemp.setImageResource(it[i].weatherIcon)
                i++

            }
            val backgroundColorId = when (temperatureDescription) {
                "Clouds" -> R.color.cloudy //Log.d("Current", "Clouds")
                "Sunny" -> R.color.sunny
                "Rain" -> R.color.rainy
                "Thunderstorm" -> R.color.rainy
                "Clear" -> R.color.sunny
                else -> R.color.white
            }


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, backgroundColorId)
            }
            val mainView = findViewById<View>(R.id.main)
            mainView.setBackgroundColor(ContextCompat.getColor(this, backgroundColorId))
            weatherAdapter.notifyDataSetChanged()


        })
        //
        progressBar.visibility = View.GONE
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddCityActivity::class.java)
            startActivity(intent)
        }
    }

    private fun RequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }
    }

    private fun getCurrentWeather(city: String) {
        //room view model
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        //filter by current location
        viewModel.setCurrentCity(city)

        viewModel.currentWeather.observe(this, Observer { user ->

            //---Method 1
            val tempDesc: String
            val tempTitle: String
            val arrayOf_tect = arrayOf(user.weather)
            val test = arrayOf_tect.get(0).get(0).description
            tempTitle = arrayOf_tect.get(0).get(0).main
            tempDesc = "${test}"
            //Round to 1 decimal
            val convertedTempCurrent = convertTemperature(user.main.temp)
            val convertedTempMax = convertTemperature(user.main.tempMax)
            val convertedTempMin = convertTemperature(user.main.tempMin)
            val convertedFeelLike = convertTemperature(user.main.feelsLike)
            //convert updateAt from unix to date time
            val timestamp = user.dt
            val netDate = Date(timestamp.toLong() * 1000)
            val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
            val updatedAt = sdf.format(netDate)
            //textViewUpdatedAt.setText("Updated ${updatedAt}")

            //change the temp image based on condition and theme color


            val resourceId = when (tempTitle) {
                "Clouds" -> R.drawable.forest_cloudy //Log.d("Current", "Clouds")
                "Sunny" -> R.drawable.forest_sunny
                "Rain" -> R.drawable.forest_rainy
                "Clear" -> R.drawable.forest_sunny
                "Thunderstorm" -> R.drawable.forest_rainy
                else -> R.drawable.default_list_image
            }

            addCurrentData(
                city,
                convertedTempMin,
                convertedTempCurrent,
                convertedTempMax,
                tempTitle,
                tempDesc,
                resourceId,
                convertedFeelLike, updatedAt
            )

        })


    }

    private fun convertTemperature(tempValue: Double): Long {
        val temperatureValue: Double = tempValue.toDouble()
        val roundTemp = Math.round(temperatureValue)
        return roundTemp
    }

    private fun addCurrentData(
        place: String,
        temp_min: Long,
        temp_current: Long,
        temp_max: Long,
        temp: String,
        tempDesc: String,
        icon: Int,
        feelLike: Long,
        updatedAt: String
    ) {
        val currentWeather = CurrentWeather(
            0,
            place,
            temp_min,
            temp_current,
            temp_max,
            temp,
            tempDesc,
            icon,
            feelLike, updatedAt
        )

        forecastViewModel.insertCurrent(currentWeather)

    }

    private fun addForecast(
        forecastDate: String,
        temperature: Double,
        temperatureDescription: String,
        weatherIcon: Int,
        timestamp: Int
    ) {
        //init ForecastViewModel room database

        val forecastData =
            Forecast(0, forecastDate, temperature, temperatureDescription, weatherIcon, timestamp)
        forecastViewModel.insertForecast(forecastData)
        //weatherAdapter.setListData(forecastData)
        //weatherAdapter.notifyDataSetChanged()


    }


    private fun getForecast(city: String) {
        //testing Forecast
        forecastViewModelApi = ViewModelProvider(this).get(ForecastViewModelApi::class.java)
        forecastViewModelApi.setCurrentCity(city)

        forecastViewModelApi.forecast.observe(this, Observer { user ->

            val total_list = user.list.size
            var i = 0 //select day(tommorrow at twelf) at 12 pm
            var tempMax1: Double
            var date1: String
            var timeStamp: Int
            var weatherMain: String
            var weatherDescription: String
            var icon: String
            //dateTime to unix
            val currentTime = Calendar.getInstance().time
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
            val updatedAt = sdf.format(currentTime)
            val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
            val date = format.parse("$updatedAt")
            val unix_timestamp = date.time / 1000

            //compare dates
            val currentDate = Calendar.getInstance().time
            val sdf2 = SimpleDateFormat("yyyy-MM-dd")
            val convertedDate = sdf2.format(currentDate)

            //time
            val formatTime = SimpleDateFormat("H")
            val convertedTime = formatTime.format(currentDate).toInt()


            while (i < total_list) {

                date1 = user.list.get(i).dtTxt
                Log.d("time", "getForecast: $convertedTime index=$i  date =   $date1")
                if (!date1.contains(convertedDate)) {
                    var selectRecentData = when (convertedTime) {
                        in 0..3 -> "00:00"
                        in 3..6 -> "03:00"
                        in 6..9 -> "06:00"
                        in 9..12 -> "09:00"
                        in 12..15 -> "09:00"
                        in 15..18 -> "12:00"
                        in 18..21 -> "18:00"
                        else -> "12:00"
                    }
                    if (date1.contains("$selectRecentData")) {

                        timeStamp = user.list.get(i).dt
                        tempMax1 = user.list.get(i).main.tempMax
                        weatherMain = user.list[i].weather[0].main
                        weatherDescription = user.list[i].weather[0].description
                        icon = user.list[i].weather[0].icon
                        Log.d(
                            "thamsanqa",
                            "apiTimeStamp:$timeStamp  appTime: $unix_timestamp   Date:$date1 index=$i  temp= $tempMax1"
                        )
                        val resourceId = when (weatherMain) {
                            "Clouds" -> R.drawable.partlysunny
                            "Clear" -> R.drawable.clear
                            "Sunny" -> R.drawable.clear
                            "Rain" -> R.drawable.rain
                            "Thunderstorm" -> R.color.rainy
                            else -> R.drawable.rain
                        }
                        addForecast(date1, tempMax1, weatherDescription, resourceId, timeStamp)
                    }
                }

                i += 1
            }


        })

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
        forecastViewModelApi.cancelJobs()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        var addresses: List<Address>

                        fusedLocationProviderClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                mLocation = location
                                if (location != null) {
                                    current_lat = location.latitude
                                    current_long = location.longitude

                                    addresses =
                                        geocoder.getFromLocation(current_lat, current_long, 1)

                                    val address: String = addresses[0].locality
                                    //Assigning latLong to find current weather
                                    getCurrentWeather(address)
                                    getForecast(address)
                                }
                            }

                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    fab.hide()
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()

                    finish()
                    System.exit(0);
                }
                return
            }
        }
    }


}

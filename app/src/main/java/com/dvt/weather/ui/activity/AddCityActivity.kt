package com.dvt.weather.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.dvt.weather.R
import com.dvt.weather.adaptor.FavoriteAdaptor
import com.dvt.weather.database.FavoriteModel
import com.dvt.weather.repository.ForecastViewModel
import com.dvt.weather.restApi.CurrentWeather.Main
import com.dvt.weather.restApi.VolleySingleton
import com.dvt.weather.utils.Constants
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class AddCityActivity : AppCompatActivity() {

    lateinit var favoriteAdaptor: FavoriteAdaptor
    val item = ArrayList<FavoriteModel>()
    private lateinit var forecastViewModel: ForecastViewModel
    lateinit var selectPlace: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        val apiKey = getString(R.string.google_maps_key)


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        // Create a new Places client instance.
        val placesClient = Places.createClient(this)
        // Initialize the AutocompleteSupportFragment.
        val fabAddCity = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplte) as AutocompleteSupportFragment?
        autocompleteFragment!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setCountries("ZA")
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN)
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("AddAutocomplete", "Place: " + place.name + ", " + place.id)
                selectPlace = "${place.name}"

                if (selectPlace == "") {
                    Toast.makeText(applicationContext, "Please select a place", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    getTemperatureByPlaceName(selectPlace)
                }
            }

            override fun onError(status: Status) {
                Log.i("AddAutocomplete", "Place: ${status.statusMessage}")
                Toast.makeText(
                    applicationContext,
                    "Place: ${status.statusMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        favoriteAdaptor = FavoriteAdaptor(mutableListOf())


        //init ForecastViewModel Room database
        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        forecastViewModel.getFavorite().observe(this, Observer<List<FavoriteModel>> { favorite ->
            favoriteAdaptor.clearListData()
            favoriteAdaptor.setListData(favorite)
            favoriteAdaptor.notifyDataSetChanged()
        })

        recyclerView.adapter = favoriteAdaptor
    }

    //TODO
    private fun getTemperatureByPlaceName(city: String) {
        // url to get json object
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=${Constants.API_KEY}"

        // request json object response from the provided url
        val request = JsonObjectRequest(
            Request.Method.GET, // method
            url, // url
            null, // json request
            { response -> // response listener

                try {
                    val obj: JSONObject = response
                    val json = obj.getJSONObject("main")
                    val gson = Gson()

                    val placeTemp: Main = gson.fromJson(json.toString(), Main::class.java)
                    Log.i("Place Temperature", placeTemp.temp.toString())

                    val dataFavarite = FavoriteModel(0, selectPlace, placeTemp.temp)
                    forecastViewModel.insertFavorite(dataFavarite)
                    Toast.makeText(this, "Favorite place successfully added", Toast.LENGTH_SHORT)
                        .show()
                    favoriteAdaptor.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.i("Place Temperature", e.toString())
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }

            },
            { error -> // error listener
                Log.i("Place Temperature", error.toString())
                Toast.makeText(applicationContext, "City name is incorrect", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        // add network request to volley queue
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(request)
    }

}

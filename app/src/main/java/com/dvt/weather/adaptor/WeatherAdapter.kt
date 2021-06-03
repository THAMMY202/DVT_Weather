package com.dvt.weather.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.dvt.weather.R
import com.dvt.weather.database.Forecast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter( private val items: MutableList<Forecast>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_row, parent, false)
    return ViewHolder(view)
  }
  fun setListData(data: List<Forecast>) {
    this.items.addAll(data)
    notifyDataSetChanged()
  }
  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    holder.bind(items[position])
    holder.imageViewPlayer.setImageResource(items[position].weatherIcon)

  }
  @SuppressLint("NewApi")
  public fun getConvertedDay(date: String): String {

    return SimpleDateFormat(
      "EEEE",
      Locale.ENGLISH
    ).format(SimpleDateFormat("YYYY-MM-DD HH:MM:ss").parse(date))
  }
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageViewPlayer: ImageView = itemView.findViewById(R.id.imageViewPlayer)
    val textViewPlayerName: TextView = itemView.findViewById(R.id.textViewPlace)
    val textViewPlayerCountry: TextView = itemView.findViewById(R.id.textViewTemperature)



      fun bind(data: Forecast){

        try {
         val forecastDay= SimpleDateFormat(
           "EEEE",
           Locale.ENGLISH
         ).format(SimpleDateFormat("yyyy-MM-dd ").parse(data.forecastDate))
          textViewPlayerName.text =  forecastDay
        } catch (e: ParseException) {
          e.printStackTrace()
        }


//Round to 1 dec
          //var temp_value:String = data.temperature_max
          //val temperatureValue:Double = temp_value.toDouble()
          val roundTemp = Math.round(data.temperature)
          textViewPlayerCountry.text = "$roundTemp °"//data.weatherIcon


      }
  }
}
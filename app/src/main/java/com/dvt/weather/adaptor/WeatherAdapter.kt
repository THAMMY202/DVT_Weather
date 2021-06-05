package com.dvt.weather.adaptor

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.*
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dvt.weather.R
import com.dvt.weather.database.Forecast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class WeatherAdapter(private val items: MutableList<Forecast>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    lateinit var mContext: Context
    lateinit var root_layout: ViewGroup
    lateinit var dialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        root_layout = parent
        dialog = Dialog(parent.context)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_row, parent, false)
        return ViewHolder(view)
    }

    fun clearListData() {
        this.items.clear()
        notifyDataSetChanged()
    }

    fun setListData(data: List<Forecast>) {
        this.items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(items[position])
        holder.imageViewTemperatureIcon.setImageResource(items[position].weatherIcon)

        // Set a click listener for button widget
        holder.itemView.setOnClickListener {
            var selectedPosition: Int = holder.adapterPosition
            var forecast: Forecast = items[selectedPosition]

            dialog.setContentView(R.layout.weather_details)

            try {
                val forecastDay = SimpleDateFormat(
                    "EEEE",
                    Locale.ENGLISH
                ).format(SimpleDateFormat("yyyy-MM-dd ").parse(forecast.forecastDate))

                var image: ImageView = dialog.findViewById(R.id.detailsImageView)

                val resourceId = when (forecast.temperatureDescription) {
                    "Clouds" -> R.drawable.forest_cloudy
                    "Sunny" -> R.drawable.forest_sunny
                    "Rain" -> R.drawable.forest_rainy
                    "Clear" -> R.drawable.forest_sunny
                    "Thunderstorm" -> R.drawable.forest_rainy
                    else -> R.drawable.default_list_image
                }

                //image.setImageResource(resourceId)

                var text: TextView = dialog.findViewById(R.id.detailsWeatherTextView)

                text.text =
                    "On  $forecastDay of the ${forecast.forecastDate} the weather temperature  will be ${
                        Math.round(forecast.temperature)
                    } °C ( ${forecast.temperatureDescription})"

                dialog.show()

            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageViewTemperatureIcon: ImageView =
            itemView.findViewById(R.id.imageViewTemperatureIcon)
        private val textViewPlaceName: TextView = itemView.findViewById(R.id.textViewPlace)
        private val textViewTemperature: TextView = itemView.findViewById(R.id.textViewTemperature)

        fun bind(data: Forecast) {

            try {
                val forecastDay = SimpleDateFormat(
                    "EEEE",
                    Locale.ENGLISH
                ).format(SimpleDateFormat("yyyy-MM-dd ").parse(data.forecastDate))
                textViewPlaceName.text = forecastDay
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val roundTemp = Math.round(data.temperature)
            textViewTemperature.text = "$roundTemp °"
        }
    }
}
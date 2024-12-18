package com.example.firstapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapp.R
import com.example.firstapp.databinding.ForecastViewholderBinding
import com.example.firstapp.model.ForecastResponseApi
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    private lateinit var binding: ForecastViewholderBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ForecastViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }
    inner class ViewHolder:RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ForecastViewholderBinding.bind(holder.itemView)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeekName = when(calendar.get(Calendar.DAY_OF_WEEK)){
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }
        binding.nameDayText.text = dayOfWeekName
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val amPm = if(hour < 12) "am" else "pm"
        val hour12 = calendar.get(Calendar.HOUR)
        binding.hourText.text = hour12.toString() + amPm
        binding.tempText.text = differ.currentList[position].main?.temp?.let{ Math.round(it) }.toString()+ "°"
        val icon = when(differ.currentList[position].weather?.get(0)?.icon.toString()){
            "01d", "0n" -> "sunny"
            "02d", "02n" -> "cloudy_sunny"
            "03d", "03n" -> "cloudy_sunny"
            "04d", "04n" -> "cloudy"
            "09d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11n" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"
        }
        // Debugging logs for icon and drawable resource ID
        Log.d("ForecastAdapter", "Icon name: $icon")
        val drawableResourceId = binding.root.resources.getIdentifier(
            icon,
            "drawable",
            binding.root.context.packageName
        )

        if (drawableResourceId != 0) {
            Glide.with(binding.root.context)
                .load(drawableResourceId)
                .into(binding.pic)
        } else {
            Glide.with(binding.root.context)
                .load(R.drawable.sunny) // Replace with a valid fallback drawable.
                .into(binding.pic)
        }
        Log.d("ForecastAdapter", "Resource ID: $drawableResourceId")

        Glide.with(binding.root.context)
            .load(drawableResourceId)
            .into(binding.pic)
    }

    private val differCallback = object :DiffUtil.ItemCallback<ForecastResponseApi.data>(){
        override fun areItemsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)


}
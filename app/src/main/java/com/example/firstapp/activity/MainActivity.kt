package com.example.firstapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
import com.example.firstapp.adapter.ForecastAdapter
import com.example.firstapp.databinding.ActivityMainBinding
import com.example.firstapp.model.CurrentResponseApi
import com.example.firstapp.model.ForecastResponseApi
import com.example.firstapp.shared.SharedData
import com.example.firstapp.viewmodel.WeatherViewModel
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy {Calendar.getInstance()}
    private val forecastAdapter by lazy { ForecastAdapter() }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {

//            var lat = intent.getDoubleExtra("lat", 0.0)
//            var lon = intent.getDoubleExtra("lon", 0.0)
//            var name = intent.getStringExtra("name")
//
//            if(lat  == 0.0) {
//                lat = 21.03
//                lon = 0.0
//                name = "Hanoi"
//            }
            val lat: Double = SharedData.sharedLatitude
            val lon: Double = SharedData.sharedLongitude
            val name: String = SharedData.sharedCity
            val unit: String = SharedData.unitString



            // Log the values
            Log.d("LocationInfo", "Latitude: $lat, Longitude: $lon, Name: $name")
            addCityButton.setOnClickListener{
                startActivity(Intent(this@MainActivity, AddCityActivity::class.java))
            }

            settingButton.setOnClickListener{
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }


            // Current Temp
            cityText.text = name
            weatherViewModel.loadCurrentWeather(lat, lon, unit).enqueue(object: Callback<CurrentResponseApi>{
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if(response.isSuccessful) {
                        val data = response.body()
                        detailLayout.visibility= View.VISIBLE
                        data?.let{
                            statusText.text = it.weather?.get(0)?.main ?: "-"
                            windText.text = (it.wind?.speed?.let { speed -> Math.round(speed).toString() } ?: "0") + "Km"
                            humidityText.text = it.main?.humidity?.toString() + "%"
                            currentTempText.text = (it.main?.temp?.let { temp -> Math.round(temp).toString() } ?: "-") + "°"
                            maxTempText.text = (it.main?.tempMax?.let { temp -> Math.round(temp).toString() } ?: "-") + "°"
                            minTempText.text = (it.main?.tempMin?.let { temp -> Math.round(temp).toString() } ?: "-") + "°"+ "°"
                            val drawable = if(isNightNow()) R.drawable.night_bg
                            else {
                                setDynamicallyWallpaper(it.weather?.get(0)?.icon?:"-")
                            }
                            backgroundImage.setImageResource(drawable)
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            // Setting Blur View
            val radius = 10f
            val decorView = window.decorView
            val rootView: ViewGroup = (decorView.findViewById(android.R.id.content))
            val windowBackground = (decorView.background)
            rootView.let{
                blurView.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blurView.clipToOutline = true
            }


            // Forecast temperature
            weatherViewModel.loadForecastWeather(lat,lon,unit).enqueue(object :Callback<ForecastResponseApi>{
                override fun onResponse(
                    call: Call<ForecastResponseApi>,
                    response: Response<ForecastResponseApi>
                ) {
                    if(response.isSuccessful){
                        val data = response.body()
                        blurView.visibility = View.VISIBLE

                        data?.let {
                            forecastAdapter.differ.submitList(it.list)
                            forecastView.apply {
                                layoutManager = LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = forecastAdapter
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun isNightNow() : Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }
    private fun setDynamicallyWallpaper(icon:String):Int{
        return when(icon.dropLast(1)){
            "01" -> {
                R.drawable.snow_bg
            }
            "02", "03", "04" ->{
                R.drawable.cloudy_bg
            }
            "09", "10", "11" ->{
                R.drawable.rainy_bg
            }
            "13"->{
                R.drawable.snow_bg
            }
            "50"->{
                R.drawable.haze_bg
            }
            else -> 0
        }
    }
}
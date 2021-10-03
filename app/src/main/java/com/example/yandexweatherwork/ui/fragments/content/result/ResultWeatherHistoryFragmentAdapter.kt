package com.example.yandexweatherwork.ui.fragments.content.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.domain.data.DataWeather

class ResultWeatherHistoryFragmentAdapter: RecyclerView.
Adapter<ResultWeatherHistoryFragmentAdapter.HistoryViewHolder>() {
//    private var weatherData: List<DataWeather> = listOf()
    private var uniqueCitiesNames: List<String> = listOf()
/*    fun setWeather(data: List<DataWeather>){
        weatherData = data
        notifyDataSetChanged()
    }*/
    fun setUniqueListCities(uniqueCitiesNames: List<String>){
        this.uniqueCitiesNames = uniqueCitiesNames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val holder = HistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_list_cities_recycler_item, parent, false)
        )
        return holder
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
//        holder.render(weatherData[position])
        holder.render(uniqueCitiesNames[position])
    }

//    override fun getItemCount() = weatherData.size
    override fun getItemCount() = uniqueCitiesNames.size

    inner class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
/*    fun render(dataWeather: DataWeather){
            dataWeather.city?.let {
                itemView.findViewById<TextView>(R.id.recycler_item_text_view)
                    .text = it.name
            }
        }*/
        fun render(uniqueCityName: String){
            itemView.findViewById<TextView>(R.id.recycler_item_text_view).text = uniqueCityName
        }
    }
}
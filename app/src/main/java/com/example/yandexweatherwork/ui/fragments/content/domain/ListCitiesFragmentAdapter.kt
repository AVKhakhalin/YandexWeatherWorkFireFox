package com.example.yandexweatherwork.ui.fragments.content.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.domain.data.City

class ListCitiesFragmentAdapter: RecyclerView.Adapter<ListCitiesFragmentAdapter.MainFragmentViewHolder>() {

    private var weatherData: List<City> = listOf()
    private lateinit var  listener: OnItemViewClickListener
    fun setWeather(data:List<City>){
        weatherData = data
        notifyDataSetChanged()
    }

    fun setOnItemViewClickListener(onItemViewClickListener:OnItemViewClickListener) {
        listener = onItemViewClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder =
        MainFragmentViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_list_cities_recycler_item,parent,false))

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) =
        holder.render(weatherData[position])

    override fun getItemCount() = weatherData.size

    inner class MainFragmentViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun render(city: City){
            with(itemView) {
                findViewById<TextView>(R.id.recycler_item_text_view).text = city.name
                setOnClickListener{
                    listener.onItemClick(city)
                }
            }
        }
    }
}
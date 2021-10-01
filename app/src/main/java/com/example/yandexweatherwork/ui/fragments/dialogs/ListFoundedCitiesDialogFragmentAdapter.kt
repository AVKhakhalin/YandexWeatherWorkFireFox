package com.example.yandexweatherwork.ui.fragments.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class ListFoundedCitiesDialogFragmentAdapter(
    private val newCitiesInfoFiltred: MutableList<CityDTO>?,
    private val listCitiesFragment: ListCitiesFragment,
    private val listFoundedCitiesDialogFragment: ListFoundedCitiesDialogFragment
): RecyclerView.Adapter<ListFoundedCitiesDialogFragmentAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var newCitiesInfoFiltredView: TextView? = null

        init {
            newCitiesInfoFiltredView = itemView.findViewById(R.id.recycler_item_text_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_list_cities_recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (newCitiesInfoFiltred != null) {
            holder.newCitiesInfoFiltredView?.text = newCitiesInfoFiltred[position].display_name
            holder.newCitiesInfoFiltredView?.setOnClickListener {
                val indexLastZapity: Int = newCitiesInfoFiltred[position]
                    .display_name.lastIndexOf(", ") + 2
                val country: String =
                    newCitiesInfoFiltred[position].display_name.subSequence(indexLastZapity,
                        newCitiesInfoFiltred[position].display_name.length) as String
                val indexFirstZapity: Int = newCitiesInfoFiltred[position]
                    .display_name.indexOf(",")
                val cityName: String = newCitiesInfoFiltred[position].display_name
                    .subSequence(0, indexFirstZapity) as String

                listCitiesFragment.addCitiesAndUpdateList(
                    City("${cityName}",
                        "${newCitiesInfoFiltred[position].lat}".toDouble(),
                        "${newCitiesInfoFiltred[position].lon}".toDouble(),
                        "$country"
                    )
                )
                listFoundedCitiesDialogFragment.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        if (newCitiesInfoFiltred != null) {
            return newCitiesInfoFiltred.size
        } else {
            return 0
        }
    }
}
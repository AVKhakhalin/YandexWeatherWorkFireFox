package com.example.yandexweatherwork.ui.fragments.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class ListContactFoundedCitiesDialogFragmentAdapter(
    private val newContactCitiesInfoFiltred: MutableList<String>?,
    private val navigationDialogs: NavigationDialogs,
    private val listContactFoundedCitiesDialogFragment: ListContactFoundedCitiesDialogFragment
): RecyclerView.Adapter<ListContactFoundedCitiesDialogFragmentAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var newCitiesInfoFiltredView: TextView? = null
        var newCitiesInfoFiltredViewContainer: ConstraintLayout? = null

        init {
            newCitiesInfoFiltredView = itemView.findViewById(R.id.recycler_item_text_view)
            newCitiesInfoFiltredViewContainer = itemView.findViewById(R.id.recycler_item_text_view_container)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_list_cities_recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (newContactCitiesInfoFiltred != null) {
            holder.newCitiesInfoFiltredView?.text = newContactCitiesInfoFiltred[position]
            holder.newCitiesInfoFiltredViewContainer?.setOnClickListener {
                navigationDialogs.showAddCityDialogFragment(
                    listContactFoundedCitiesDialogFragment.requireActivity(),
                    newContactCitiesInfoFiltred[position])
                listContactFoundedCitiesDialogFragment.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        if (newContactCitiesInfoFiltred != null) {
            return newContactCitiesInfoFiltred.size
        } else {
            return 0
        }
    }
}
package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class ListFoundedCitiesDialogFragment(
    private val newCitiesInfoFiltred: MutableList<CityDTO>?,
    private val listCitiesFragment: ListCitiesFragment,
    private val navigationDialogs: NavigationDialogs
): DialogFragment(), DialogInterface.OnClickListener {

    private var buttonNo: Button? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_list_founded_cities, null)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.fragment_list_founded_cities_RecyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView!!.adapter = ListFoundedCitiesDialogFragmentAdapter(newCitiesInfoFiltred,
            listCitiesFragment, this, navigationDialogs)

        buttonNo = view.findViewById(R.id.founded_city_button_cancel)
        if (buttonNo != null) {
            buttonNo!!.setOnClickListener(View.OnClickListener { view: View ->
                onNo(view)
            })
        }
    }

    // Результат нажатия на кнопку отмены действия
    private fun onNo(view: View) {
        dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
    }
}
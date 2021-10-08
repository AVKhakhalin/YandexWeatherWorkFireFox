package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.Context
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
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class ListContactFoundedCitiesDialogFragment(
    private val newContactCitiesInfoFiltred: MutableList<String>?,
    private val navigationDialogs: NavigationDialogs
): DialogFragment(), DialogInterface.OnClickListener {

    private var buttonNo: Button? = null
    private var recyclerView: RecyclerView? = null
    private var navigationContent: NavigationContent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получение навигатора для загрузки фрагментов с основным содержанием приложения (Content)
        navigationContent = (context as MainActivity).getNavigationContent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(
            R.layout.dialog_fragment_list_contact_founded_cities, null)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.fragment_list_contact_founded_cities_RecyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView!!.adapter =
            ListContactFoundedCitiesDialogFragmentAdapter(newContactCitiesInfoFiltred,
                navigationDialogs, navigationContent, this)

        buttonNo = view.findViewById(R.id.contact_founded_city_button_cancel)
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
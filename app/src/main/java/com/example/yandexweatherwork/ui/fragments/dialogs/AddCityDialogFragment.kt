package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.repository.facadeuser.RepositoryGetCityInfo
import com.example.yandexweatherwork.ui.ConstantsUi
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class AddCityDialogFragment(
    private val listCitiesFragment: ListCitiesFragment,
    private val defaultPlace: String?,
    private val defaultLatitude: Double?,
    private val defaultLongitude: Double?
): DialogFragment(), DialogInterface.OnClickListener {
    private var buttonYes: Button? = null
    private var buttonNo: Button? = null
    private var addCityNameField: EditText? = null
    private var addLatField: EditText? = null
    private var addLonField: EditText? = null
    private var addCountryField: EditText? = null

    private var navigationDialogs: NavigationDialogs? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получение навигатора для загрузки диалоговых фрагментов (Dialogs)
        navigationDialogs = (context as MainActivity).getNavigationDialogs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_add_city, null)
        initView(view)
        addCityNameField = view.findViewById(R.id.add_city_info_city_field)
        addLatField = view.findViewById(R.id.add_city_info_lat_field)
        addLonField = view.findViewById(R.id.add_city_info_lon_field)
        addCountryField = view.findViewById(R.id.add_city_info_country_field)

        // Установка заранее известного места (например, найденного в контактах)
        if ((addCityNameField != null) && (addLatField != null)
            && (addLonField  != null) && (addCountryField != null)) {
            if (defaultPlace != null) {
                addCityNameField!!.setText(defaultPlace)
                var positionOfLastZapitay: Int = defaultPlace.lastIndexOf(", ")
                if (positionOfLastZapitay > -1) {
                    positionOfLastZapitay += 2
                    addCountryField!!.setText(defaultPlace.subSequence(positionOfLastZapitay,
                        defaultPlace.length))
                } else {
                    addCountryField!!.setText("")
                }
            } else {
                addCityNameField!!.setText("")
                addCountryField!!.setText("")
            }
            if (defaultLatitude != null) {
                addLatField!!.setText(defaultLatitude.toString())
            } else {
                addLatField!!.setText("")
            }
            if (defaultLongitude != null) {
                addLonField!!.setText(defaultLongitude.toString())
            } else {
                addLonField!!.setText("")
            }
        }
        return view
    }

    private fun initView(view: View) {
        buttonYes = view.findViewById(R.id.add_city_info_button_ok)
        if (buttonYes != null) {
            buttonYes!!.setOnClickListener(View.OnClickListener { view: View ->
                onYes(view)
            })
        }
        buttonNo = view.findViewById(R.id.add_city_info_button_cancel)
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

    // Результат нажатия на кнопку подтверждения действия
    private fun onYes(view: View) {
        if ((addCityNameField != null) && (addLatField != null)
            && (addLonField  != null) && (addCountryField != null))
                if (addLatField!!.text.isNotEmpty() && addLonField!!.text.isNotEmpty()) {
                    navigationDialogs?.let {
                        if (it.getterMapsFragment() != null) {
                            it.getterNavigationContent()?.let { navContent ->
                                navContent.getMainChooserSetter().addKnownCities(City(
                                    "${addCityNameField!!.text}",
                                    "${addLatField!!.text}".toDouble(),
                                    "${addLonField!!.text}".toDouble(),
                                    "${addCountryField!!.text}"
                                ))
                                // Установка обновлённого фильтра страны по-умолчанию
                                if ("${addCountryField!!.text}".lowercase() ==
                                    ConstantsUi.FILTER_RUSSIA.lowercase()) {
                                    navContent.getMainChooserSetter().setDefaultFilterCountry(
                                        ConstantsUi.FILTER_RUSSIA)
                                } else {
                                    navContent.getMainChooserSetter().setDefaultFilterCountry(
                                        ConstantsUi.FILTER_NOT_RUSSIA)
                                }
                            }
                            it.getterMapsFragment()!!.mapUpdate()
                        } else {
                            listCitiesFragment.addCitiesAndUpdateList(
                                City(
                                    "${addCityNameField!!.text}",
                                    "${addLatField!!.text}".toDouble(),
                                    "${addLonField!!.text}".toDouble(),
                                    "${addCountryField!!.text}"
                                )
                            )
                        }
                    }
                } else {
                    val repositoryGetCityInfo: RepositoryGetCityInfo = RepositoryGetCityInfo()
                    val newCitiesInfoFiltred: MutableList<CityDTO>? = repositoryGetCityInfo
                        .getCityInfo(addCityNameField!!.text.toString(),
                            addCountryField!!.text.toString())
                    navigationDialogs?.let {
                        it.showListFoundedCitiesDialogFragment(newCitiesInfoFiltred,
                            requireActivity())
                    }
                }

        // Закрытие текущего диалогового фрагмента
        dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {}
}
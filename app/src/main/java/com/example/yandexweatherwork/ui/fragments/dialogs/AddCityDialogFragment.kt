package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var buttonYes: Button? = null
    private var buttonNo: Button? = null
    private var addCityNameField: EditText? = null
    private var addLatField: EditText? = null
    private var addLonField: EditText? = null
    private var addCountryField: EditText? = null
    private var switchRusNotRus: Switch? = null
    private var imageRussia: ImageView? = null
    private var imageNotRussia: ImageView? = null
    private var countryName: String = ConstantsUi.NOT_RUSSIA_NAME
    // Задание интерфеса для ввода координат (только числа, десятичная точка и минус)
    val inputTypeCoordinatesInterface = InputType.TYPE_CLASS_NUMBER or // разрешить ввод числа
            InputType.TYPE_NUMBER_FLAG_DECIMAL or // разрешить ввод десятичной точки
            InputType.TYPE_NUMBER_FLAG_SIGNED // разрешить ввод положительных и отрицательных чисел

    private var navigationDialogs: NavigationDialogs? = null
    //endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получение навигатора для загрузки диалоговых фрагментов (Dialogs)
        navigationDialogs = (context as MainActivity).getNavigationDialogs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_add_city, null)
        initView(view)

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
                    addCountryField!!.setText(ConstantsUi.FILTER_RUSSIA)
                }
            } else {
                addCityNameField!!.setText(ConstantsUi.EMPTY_NAME)
                addCountryField!!.setText(ConstantsUi.FILTER_RUSSIA)
            }
            if (defaultLatitude != null) {
                addLatField!!.setText(defaultLatitude.toString())
            } else {
                addLatField!!.setText(ConstantsUi.EMPTY_NAME)
            }
            if (defaultLongitude != null) {
                addLonField!!.setText(defaultLongitude.toString())
            } else {
                addLonField!!.setText(ConstantsUi.EMPTY_NAME)
            }
        }
        return view
    }

    private fun initView(view: View) {
        addCityNameField = view.findViewById(R.id.add_city_info_city_field)
        addLatField = view.findViewById(R.id.add_city_info_lat_field)
        addLatField?.let { it.inputType = inputTypeCoordinatesInterface }
        addLonField = view.findViewById(R.id.add_city_info_lon_field)
        addLonField?.let { it.inputType = inputTypeCoordinatesInterface }
        addCountryField = view.findViewById(R.id.add_city_info_country_field)
        addCountryField?.let { it.setText(ConstantsUi.FILTER_RUSSIA) }
        switchRusNotRus = view.findViewById(R.id.add_city_info_country_rus_notrus_switch)
        imageRussia = view.findViewById(R.id.add_city_info_country_russian_button)
        imageNotRussia = view.findViewById(R.id.add_city_info_country_notrussian_button)

        // Установка события нажатия на позитивную кнопку
        buttonYes = view.findViewById(R.id.add_city_info_button_ok)
        if (buttonYes != null) {
            buttonYes!!.setOnClickListener { view ->
                onYes(view)
            }
        }

        // Установка события нажатия на негативную кнопку
        buttonNo = view.findViewById(R.id.add_city_info_button_cancel)
        if (buttonNo != null) {
            buttonNo!!.setOnClickListener { view ->
                onNo(view)
            }
        }

        // Установка нажатия на переключатель названий стран
        if ((switchRusNotRus != null) && (imageRussia != null) && (imageNotRussia != null) &&
            (addCountryField != null)) {
            switchRusNotRus!!.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    imageRussia!!.setImageResource(R.drawable.ic_russia_gray)
                    imageNotRussia!!.setImageResource(R.drawable.ic_earth)
                    checkAndSaveNotRussianCountryName()
                    addCountryField!!.setText(countryName)
                } else {
                    imageRussia!!.setImageResource(R.drawable.ic_russia)
                    imageNotRussia!!.setImageResource(R.drawable.ic_earth_gray)
                    checkAndSaveNotRussianCountryName()
                    addCountryField!!.setText(ConstantsUi.FILTER_RUSSIA)
                }
            }
        }
    }

    // Проверка и сохранение введённого названия иностранной страны
    private fun checkAndSaveNotRussianCountryName() {
        if ((addCountryField!!.text != null) &&
            (addCountryField!!.text.toString().lowercase() !=
                    ConstantsUi.FILTER_RUSSIA.lowercase()) &&
            (addCountryField!!.text.toString() != ConstantsUi.NOT_RUSSIA_NAME)
        ) {
            countryName = addCountryField!!.text.toString()
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
                countryName = addCountryField!!.text.toString()
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
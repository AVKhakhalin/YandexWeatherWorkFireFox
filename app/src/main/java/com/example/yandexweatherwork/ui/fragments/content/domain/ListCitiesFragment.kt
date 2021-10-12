package com.example.yandexweatherwork.ui.fragments.content.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.controller.observers.domain.ListCitiesPublisherDomain
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModel
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModelSetter
import com.example.yandexweatherwork.controller.observers.viewmodels.UpdateState
import com.example.yandexweatherwork.databinding.FragmentListCitiesBinding
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.repository.facadeuser.RepositoryGetCityCoordinates
import com.example.yandexweatherwork.ui.ConstantsUi
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class ListCitiesFragment(
    private var isDataSetRusInitial: Boolean,
    private var mainChooserSetter: MainChooserSetter,
    private var mainChooserGetter: MainChooserGetter
): Fragment(), OnItemViewClickListener, ListCitiesFragmentGetter {
    private var _binding: FragmentListCitiesBinding? = null
    private val binding: FragmentListCitiesBinding
        get() {
            return _binding!!
        }
    private var listCitiesFragmentAdapter = ListCitiesFragmentAdapter(this)
    private var weather: MutableList<City>? = mutableListOf()
    private var navigationContent: NavigationContent? = null
    private var navigationDialogs: NavigationDialogs? = null

    // Ссылка на ResultCurrentViewModel
    private lateinit var listCitiesViewModel: ListCitiesViewModel

    companion object {
        fun newInstance(isDataSetRusInitial: Boolean, mainChooserSetter: MainChooserSetter,
                        mainChooserGetter: MainChooserGetter) =
            ListCitiesFragment(isDataSetRusInitial, mainChooserSetter, mainChooserGetter)
    }

    // Создание наблюдателя в domain
    private var listCitiesPublisherDomain: ListCitiesPublisherDomain = ListCitiesPublisherDomain()
    //endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Создание viewModel
        listCitiesViewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        // Задание наблюдателя для данного фрагмента (viewModel)
        (context as ListCitiesViewModelSetter).setListCitiesViewModel(listCitiesViewModel)
        // Получение наблюдателя для domain
        listCitiesPublisherDomain = (context as MainActivity).getListSitiesPublisherDomain()
        // Получение навигатора для загрузки фрагментов с основным содержанием приложения (Content)
        navigationContent = (context as MainActivity).getNavigationContent()
        // Получение навигатора для загрузки диалоговых фрагментов (Dialogs)
        navigationDialogs = (context as MainActivity).getNavigationDialogs()
        navigationDialogs?.let { it.setListCitiesFragment(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Начальная установка вида кнопки переключения фильтра стран
        if (isDataSetRusInitial) {
            binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_earth)
        } else {
            binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_russia)
        }

        binding.fragmentListCitiesRecyclerView.adapter = listCitiesFragmentAdapter
        listCitiesFragmentAdapter.setOnItemViewClickListener(this)
        binding.fragmentListCitiesFAB.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                checkAndCorrectCountryState()
            }

        })
        listCitiesViewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        listCitiesViewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<UpdateState> { updateState: UpdateState ->
                renderData(updateState)
            })
        listCitiesViewModel.getListCities()

        // Установка событий при нажатии на кнопку вызова геолокации
        binding.fragmentLocationFAB.setOnClickListener {
            checkPermission()
        }
    }

    private fun renderData(updateState: UpdateState) {
        when (updateState) {
            is UpdateState.ListCities -> {
                weather = updateState.mainChooserGetter.getKnownCites()
                weather?.let {
                    // Передача в адаптер списка городов
                    listCitiesFragmentAdapter.setWeather(weather!!)
                }
            }
            //else -> //TODO: Добавить случай с неуспешной загрузкой списка
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(city: City) {
        if (mainChooserGetter.getExistInternet()) {
            navigationContent?.let { it.showResultCurrentFragment(
                city,
                true,
                false) }
        } else {
            Snackbar.make(requireView(), "${resources.getString(R.string.error)}: " +
                    "${resources.getString(R.string.error_no_connection)}",
                Snackbar.LENGTH_LONG).show()
        }
    }

    //region МЕТОДЫ ДЛЯ РАБОТЫ С КОНТЕКСТНЫМ МЕНЮ У ЭЛЕМЕНТОВ СПИСКА
    // Создание контекстного меню для элемента списка
    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val positionChoosedElement: Int = listCitiesFragmentAdapter
            .getPositionChoosedElement()
        when (item.itemId) {
            R.id.context_menu_action_delete_city -> {
                // Удаление места (города) из списка
                // Отображение диалогового фрагмента DeleteFragment с подтверждением удаления места
                // Удаление места через контекстное меню
                weather?.let{
                    if (navigationDialogs != null) {
                        navigationDialogs?.showDeleteConformationDialogFragment(
                            positionChoosedElement, it[positionChoosedElement]!!.name,
                            it[positionChoosedElement]!!.country, this,
                            requireActivity())
                    }
                }
            }
            R.id.context_menu_action_show_card -> {
                Toast.makeText(context, "Показать карточку места ${listCitiesFragmentAdapter
                    .getPositionChoosedElement()}", Toast.LENGTH_SHORT).show()
                // Отображение диалогового фрагмента CardCity
                // для редаткирования информации о месте (городе)
                weather?.let{
                    if (navigationDialogs != null) {
                        navigationDialogs?.showCardCityDialogFragment(
                            positionChoosedElement, it!![positionChoosedElement],
                            this, requireActivity())
                    }
                }
            }
        }
        return super.onContextItemSelected(item)
    }
    //endregion

    // Метод для удаления места из списка и его обновления
    fun deleteCitiesAndUpdateList(positionChoosedElement: Int, filterCity: String,
                                  filterCountry: String) {
        mainChooserSetter?.let{
            if (weather != null) {
                it.removeCity(filterCity, filterCountry)
            }
        }

        listCitiesFragmentAdapter.notifyItemChanged(positionChoosedElement)
        weather?.let{
            it.removeAt(positionChoosedElement)
            // Передача в адаптер обновлённого списка городов
            listCitiesFragmentAdapter.setWeather(weather!!, positionChoosedElement)
            // Смена фильтра страны
            if ((it.size == 0) && (mainChooserGetter.getNumberKnownCites() > 0)) {
                checkAndCorrectCountryState()
            }
        }
        // Установка признака редактирования пользователем списка мест
        mainChooserSetter.setUserCorrectedCityList(true)
    }

    // Метод для обновления информации о месте из списка и его обновления
    fun editCitiesAndUpdateList(positionChoosedElement: Int, city: City) {
        var newCity: City = city

        // Поиск координат места в случае, если их не заполнили для данного места
        if ((newCity.lat == ConstantsUi.ERROR_COORDINATE)
            && (newCity.lon == ConstantsUi.ERROR_COORDINATE)) {
            val repositoryGetCityCoordinates: RepositoryGetCityCoordinates
            = RepositoryGetCityCoordinates(newCity.name, mainChooserSetter)
            // Запуск класса repositoryGetCityCoordinates в новом потоке
            val thread = Thread {
                try {
                    repositoryGetCityCoordinates.run()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
            // Запуск на считывание найденных координат места через 1 секунду в основном потоке
            this.view?.postDelayed(Runnable {
                newCity?.let{
                    it.lat = mainChooserGetter.getLat()
                    it.lon = mainChooserGetter.getLon()
                }
            },ConstantsUi.DELAY_MILISEC)
        }

        if (weather!![positionChoosedElement].country.lowercase() == newCity.country.lowercase()) {
            // Случай, когда страна в карточке места не поменялась
            changeWeather(newCity, positionChoosedElement)
            // Изменение в текущем списке места
            listCitiesFragmentAdapter.notifyItemChanged(positionChoosedElement)
            weather?.let {
                it[positionChoosedElement] = newCity
                // Передача в адаптер обновлённого списка городов
                listCitiesFragmentAdapter.setWeather(positionChoosedElement, weather!!)
            }
        } else {
            // Случай, когда страна в карточке места поменялась
            if (weather!![positionChoosedElement].country.lowercase()
                == ConstantsUi.FILTER_RUSSIA.lowercase()) {
                // Случай, когда изменённое место было в России
                changeWeather(newCity, positionChoosedElement)
                // Удаление места из текущего списка российских мест
                listCitiesFragmentAdapter.notifyItemChanged(positionChoosedElement)
                weather?.let {
                    it.removeAt(positionChoosedElement)
                    // Передача в адаптер обновлённого списка городов
                    listCitiesFragmentAdapter.setWeather(weather!!, positionChoosedElement)
                    // Смена нового фильтра страны, когда НЕРОССИЙСКИХ мест больше не осталось
                    if (it.size == 0) {
                        mainChooserSetter.setDefaultFilterCountry(ConstantsUi.FILTER_RUSSIA)
                        checkAndCorrectCountryState()
                    }
                }
            } else {
                // Случай, когда изменённое место было НЕ в России
                changeWeather(newCity, positionChoosedElement)
                if (newCity.country.lowercase() == ConstantsUi.FILTER_RUSSIA.lowercase()) {
                    // Удаление места из текущего списка российских мест
                    listCitiesFragmentAdapter.notifyItemChanged(positionChoosedElement)
                    weather?.let {
                        it.removeAt(positionChoosedElement)
                        // Передача в адаптер обновлённого списка городов
                        listCitiesFragmentAdapter.setWeather(weather!!, positionChoosedElement)
                        // Смена нового фильтра страны, когда НЕРОССИЙСКИХ мест больше не осталось
                        if (it.size == 0) {
                            mainChooserSetter.setDefaultFilterCountry(ConstantsUi.FILTER_NOT_RUSSIA)
                            checkAndCorrectCountryState()
                        }
                    }
                } else {
                    // Изменение в текущем списке места
                    listCitiesFragmentAdapter.notifyItemChanged(positionChoosedElement)
                    weather?.let {
                        it[positionChoosedElement] = newCity
                        // Передача в адаптер обновлённого списка городов
                        listCitiesFragmentAdapter.setWeather(positionChoosedElement, weather!!)
                    }
                }
            }
        }
        // Установка признака редактирования пользователем списка мест
        mainChooserSetter.setUserCorrectedCityList(true)
    }

    // Метод для обновления информации о месте из списка и его обновления
    fun addCitiesAndUpdateList(city: City) {
        var newCity: City = city
        if ((mainChooserGetter.getDefaultFilterCountry().lowercase() == newCity.country.lowercase())
            || ((mainChooserGetter.getDefaultFilterCountry().lowercase()
                    == ConstantsUi.FILTER_NOT_RUSSIA.lowercase())
                    && (newCity.country.lowercase() != ConstantsUi.FILTER_RUSSIA.lowercase()))){
            // Изменение в текущем списке места и в основном списке класса mainChooser
            changeWeather(newCity)
            weather?.let {
                // Передача в адаптер обновлённого списка городов
                listCitiesFragmentAdapter.addWeather(weather!!)
            }
        } else {
            mainChooserSetter.addKnownCities(newCity)
            // Смена фильтра страны
            if (mainChooserGetter.getDefaultFilterCountry().lowercase()
                == ConstantsUi.FILTER_RUSSIA) {
                mainChooserSetter.setDefaultFilterCountry(ConstantsUi.FILTER_NOT_RUSSIA)
            } else {
                mainChooserSetter.setDefaultFilterCountry(ConstantsUi.FILTER_RUSSIA)
            }
            checkAndCorrectCountryState()
        }
        // Установка признака редактирования пользователем списка мест
        mainChooserSetter.setUserCorrectedCityList(true)
    }

    // Поменять значение в классе weather в определённой позиции на обновлённый city
    private fun changeWeather(
        city: City,
        positionChoosedElement: Int
    ) {
        mainChooserSetter?.let {
            if ((weather != null) && (city != null)) {
                it.editCity(
                    weather!![positionChoosedElement].name,
                    weather!![positionChoosedElement].country, city
                )
            }
        }
    }

    // Добавить новое city в класс weather
    private fun changeWeather(
        city: City) {
        mainChooserSetter?.let {
            it.addKnownCities(city)
            if (weather != null) {
                weather!!.add(city)
            }
        }
    }

    // Проверка и корректировка фильтра выводимых мест по стране (Россия или не Россия)
    private fun checkAndCorrectCountryState() {
        mainChooserGetter?.let{
            val invertedFilterCountry: String = (if (it.getDefaultFilterCountry().lowercase()
                == ConstantsUi.FILTER_RUSSIA.lowercase()) ConstantsUi.FILTER_NOT_RUSSIA
            else ConstantsUi.FILTER_RUSSIA)
            if (it.getKnownCites("", invertedFilterCountry)!!.size > 0) {
                isDataSetRusInitial = !isDataSetRusInitial
                if(!isDataSetRusInitial){
                    binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_russia)
                    with(listCitiesPublisherDomain) {
                        notifyDefaultFilterCountry(ConstantsUi.FILTER_NOT_RUSSIA)
                        notifyDefaultFilterCity(ConstantsUi.DEFAULT_FILTER_CITY)
                    }
                    listCitiesViewModel.getListCities()
                }else {
                    binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_earth)
                    with(listCitiesPublisherDomain) {
                        notifyDefaultFilterCountry(ConstantsUi.FILTER_RUSSIA)
                        notifyDefaultFilterCity(ConstantsUi.DEFAULT_FILTER_CITY)
                    }
                    listCitiesViewModel.getListCities()
                }
            } else {
                Snackbar.make(requireView(), "${resources.getString(R.string.error)}: " +
                        "${resources.getString(R.string.error_no_such_places)}",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun getListCitiesFragment(): ListCitiesFragment {
        return this
    }

    //region МЕТОДЫ ДЛЯ РАБОТЫ С ГЕОЛОКАЦИЕЙ
    private fun checkPermission() {
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            } else if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRatio()
            } else {
                myRequestPermission()
            }
        }
    }

    private fun showRatio() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.geocoder_dialog_title)
            .setMessage(R.string.geocoder_dialog_message)
            .setPositiveButton(R.string.geocoder_dialog_positive_answer) { dialog, which ->
                myRequestPermission()
            }
            .setNegativeButton(R.string.geocoder_dialog_negative_answer) { dialog, which ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ConstantsUi.REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ConstantsUi.REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    context?.let {
                        showRatio()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val onLocationChangeListener = object: LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressAsync(requireContext(),location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        var address = geoCoder.getFromLocation(location.latitude,location.longitude,1)
        showAddressDialog(address[0].getAddressLine(0),location)
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(getString(R.string.geocoder_weather_dialog_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.geocoder_weather_positive_answer)) { _, _ ->
                    navigationDialogs?.let { it.showAddCityDialogFragment(
                        requireActivity(),
                        address,
                        location.latitude.toDouble(), location.longitude.toDouble())
                    }
                }
                .setNegativeButton(getString(R.string.geocoder_weather_negative_answer)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    fun getLocation() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as
                        LocationManager
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    val provider= locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let{
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            ConstantsUi.REFRESH_PERIOD, ConstantsUi.MINIMAL_DISTANCE,
                            onLocationChangeListener)
                    }

                } else{
                    val location = locationManager.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER)
                    location?.let{
                        // Здесь можно запросить информацию о координатах и адресе
                    }
                }
            } else {
                showRatio()
            }
        }
    }
    //endregion
}
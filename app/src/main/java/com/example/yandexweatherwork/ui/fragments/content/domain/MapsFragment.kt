package com.example.yandexweatherwork.ui.fragments.content.domain

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.databinding.FragmentMapsBinding
import com.example.yandexweatherwork.databinding.FragmentMapsContainerBinding
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.ConstantsUi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Thread.sleep

class MapsFragment(
    private val mainChooserGetter: MainChooserGetter,
    private val mainChooserSetter: MainChooserSetter,
    private val navigationContent: NavigationContent
): Fragment() {

    companion object {
        fun newInstance(mainChooserGetter: MainChooserGetter,
                        mainChooserSetter: MainChooserSetter,
                        navigationContent: NavigationContent) =
            MapsFragment(mainChooserGetter, mainChooserSetter, navigationContent)
    }

    private var _binding: FragmentMapsContainerBinding? = null
    private val binding: FragmentMapsContainerBinding
        get() {
            return _binding!!
        }

    lateinit var map:GoogleMap

    private var mapFragment: SupportMapFragment? = null
    private val startPlaceCoord: Array<Double> = arrayOf(ConstantsUi.START_POINT_LATITUDE,
        ConstantsUi.START_POINT_LONGITUDE)
    private var isStartMarker: Boolean = false

    private val markers: ArrayList<Marker> = arrayListOf()
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        val startPlace = LatLng(startPlaceCoord[0], startPlaceCoord[1])
        if (!isStartMarker) {
            map.addMarker(MarkerOptions().position(startPlace).title(ConstantsUi.START_POINT_NAME))
            isStartMarker = true
        } else {
            // Удаление предыдущих маркеров
            deleteAllMarkers()
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(startPlace))
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true

        val isPermissionGranted =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        map.setMyLocationEnabled(isPermissionGranted)

        map.uiSettings.isMyLocationButtonEnabled = true

        // Добавление нового места к уже известным местам
        map.setOnMapLongClickListener { location ->
            startPlaceCoord[0] = location.latitude
            startPlaceCoord[1] = location.longitude
            moveToPosition(location)
            navigationContent.getterNavigationDialogs()?.let {
                it.showAddCityDialogFragment(requireActivity(),
                    null,
                    location.latitude,
                    location.longitude)
            }
        }

        // Установка на карте флажков для обозначения известных мест
        val knowCities: List<City>? = mainChooserGetter.getKnownCites()
        knowCities.let { it!!.forEach { city ->
                addMarker(LatLng(city.lat, city.lon), city.name)
            }
        }

        // Отображение окна с названием места (города) над маркером
        map.setOnMarkerClickListener { marker ->
            if (marker.title == "") {
                navigationContent.showResultCurrentFragment(
                    City(marker.title,
                        marker.position.latitude,
                        marker.position.longitude,
                        ""), true, false)
            } else {
                marker.showInfoWindow()
            }
            true
        }

        // Загрузка погодных данных при клике на окно с деталями маркера места (города)
        map.setOnInfoWindowClickListener { marker ->
            navigationContent.showResultCurrentFragment(
                City(marker.title,
                    marker.position.latitude,
                    marker.position.longitude,
                    ""), true, false)
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsContainerBinding.inflate(inflater, container, false)
        navigationContent.setterMapsFragment(this)
        navigationContent.getterNavigationDialogs()?.let { it.setterMapsFragment(this)}

        // Начальная установка вида кнопки переключения фильтра стран
        if (navigationContent.getMainChooserGetter().
            getDefaultFilterCountry().lowercase() == ConstantsUi.FILTER_RUSSIA) {
            binding.fragmentMapsFragmentListCitiesFAB.setImageResource(R.drawable.ic_earth)
        } else {
            binding.fragmentMapsFragmentListCitiesFAB.setImageResource(R.drawable.ic_russia)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Поиск на карте нового места
        binding.fragmentMapsButtonPlaceSearch.setOnClickListener {
            if ((binding.fragmentMapsSearchAddressField.text != null) &&
                (binding.fragmentMapsSearchAddressField.text.toString() != "")) {
                val geocoder = Geocoder(requireContext())
                val addressRow = binding.fragmentMapsSearchAddressField.text.toString()
                val address = geocoder.getFromLocationName(addressRow, 1)
                var location: LatLng? = null
                if (address.size > 0) {
                    location = LatLng(address[0].latitude, address[0].longitude)
                    moveToPosition(location)
                } else {
                    Snackbar.make(requireView(), "${resources.getString(R.string.error)}: " +
                            "${resources.getString(
                                R.string.error_maps_search_address_not_success)}",
                        Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(requireView(), "${resources.getString(R.string.error)}: " +
                        "${resources.getString(R.string.error_maps_search_address)}",
                    Snackbar.LENGTH_LONG).show()
            }
        }

        // Поиск на карте нового места и его сохранение (добавление) в список мест
        binding.fragmentMapsButtonPlaceSearchAndSave.setOnClickListener {
            if ((binding.fragmentMapsSearchAddressField.text != null) &&
                (binding.fragmentMapsSearchAddressField.text.toString() != "")) {
                val geocoder = Geocoder(requireContext())
                val addressRow = binding.fragmentMapsSearchAddressField.text.toString()
                val address = geocoder.getFromLocationName(addressRow, 1)
                var location: LatLng? = null
                if (address.size > 0) {
                    location = LatLng(address[0].latitude, address[0].longitude)
                    moveToPosition(location!!)

                    // Добавление найденного места (города)
                    startPlaceCoord[0] = location!!.latitude
                    startPlaceCoord[1] = location!!.longitude
                    moveToPosition(location!!)
                    navigationContent.getterNavigationDialogs()?.let {
                        it.showAddCityDialogFragment(requireActivity(),
                            addressRow,
                            location!!.latitude,
                            location!!.longitude)
                    }
                } else {
                    Snackbar.make(requireView(), "${resources.getString(R.string.error)}: " +
                            "${resources.getString(
                                R.string.error_maps_search_address_not_success)}",
                        Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(requireView(), "${resources.getString(R.string.error)}: " +
                        "${resources.getString(R.string.error_maps_search_address)}",
                    Snackbar.LENGTH_LONG).show()
            }
        }

        binding.fragmentMapsFragmentListCitiesFAB.setOnClickListener {
            switchCountryState()
            deleteAllMarkers()
            mapFragment?.getMapAsync(callback)
        }
    }

    fun mapUpdate() {
        mapFragment?.let {
            it.getMapAsync(callback)
        }
    }

    // Проверка и корректировка фильтра выводимых мест по стране (Россия или не Россия)
    private fun switchCountryState() {
        if ((mainChooserGetter != null) && (mainChooserSetter != null)) {
            val invertedFilterCountry: String =
                (if (mainChooserGetter.getDefaultFilterCountry().lowercase()
                == ConstantsUi.FILTER_RUSSIA.lowercase()
            ) ConstantsUi.FILTER_NOT_RUSSIA
            else ConstantsUi.FILTER_RUSSIA)
            mainChooserSetter.setDefaultFilterCountry(invertedFilterCountry)

            // Смена иконки на кнопке при переключении фильтра страны
            if (invertedFilterCountry.lowercase() == ConstantsUi.FILTER_RUSSIA.lowercase()) {
                binding.fragmentMapsFragmentListCitiesFAB.setImageResource(R.drawable.ic_earth)
            } else {
                binding.fragmentMapsFragmentListCitiesFAB.setImageResource(R.drawable.ic_russia)
            }
        }
    }

    //region Функции для рисования на карте
    private fun deleteAllMarkers() {
        map.clear()
    }

    private fun addMarker(location: LatLng, cityName: String) {
        markers.add(
            map.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_flag))
                    .position(location)
                    .title(cityName)
            )
        )
    }

    private fun moveToPosition(location: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
            ConstantsUi.ZOOM_VALUE_TO_POSITION))
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        navigationContent.setterMapsFragment(null)
        navigationContent.getterNavigationDialogs()?.let { it.setterMapsFragment(null)}
    }
}
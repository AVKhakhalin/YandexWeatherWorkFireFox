package com.example.yandexweatherwork.ui.fragments.content.result

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.controller.observers.domain.PublisherDomain
import com.example.yandexweatherwork.controller.observers.viewmodels.ResultCurrentViewModel
import com.example.yandexweatherwork.controller.observers.viewmodels.ResultCurrentViewModelSetter
import com.example.yandexweatherwork.controller.observers.viewmodels.UpdateState
import com.example.yandexweatherwork.databinding.FragmentResultCurrentBinding
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.data.Fact
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.repository.ConstantsRepository
import com.example.yandexweatherwork.repository.facadeuser.GetDataFromInternetService
import com.example.yandexweatherwork.ui.ConstantsUi
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar

class ResultCurrentFragment(
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    // Данные о месте (городе)
    private var city: City
): Fragment() {

    // Фабричный метод создания фрагмента
    companion object {
        fun newInstance(city: City) = ResultCurrentFragment(city)
    }

    // Ссылка на ResultCurrentViewModel
    private lateinit var resultCurrentViewModel: ResultCurrentViewModel

    // Создание binding с возможностью удаления (имя класса FragmentResultCurrentBinding формируется из класса ResultCurrentFragment)
    // Класс FragmentResultCurrentBinding - представление макета fragment_result_current.xml в виде кода
    private var bindingReal: FragmentResultCurrentBinding? = null
    private val bindingNotReal: FragmentResultCurrentBinding
        get() {
            return bindingReal!!
        }

    // Создание наблюдателя в domain
    var publisherDomain: PublisherDomain = PublisherDomain()
    // Создание навигатора для создания фрагментов с основной информацией приложения (Content)
    private var navigationContent: NavigationContent? = null
    // Получение геттерова и сеттерова на MainChooser
    private var mainChooserGetter: MainChooserGetter? = null
    private var mainChooserSetter: MainChooserSetter? = null
    // Погодные данные
    private var dataWeather: DataWeather? = null
    private var fact: Fact? = null
    //endregion


    private val receiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { it ->
                    // Отслеживание состояния загрузки погодных данных
                    dataWeather = it.getParcelableExtra<DataWeather>(ConstantsRepository.WEATHER_DATA)
                    resultCurrentViewModel.getDataFromRemoteSource(dataWeather, city)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Создание viewModel
        resultCurrentViewModel = ViewModelProvider(this).get(ResultCurrentViewModel::class.java)
        resultCurrentViewModel.setActivityContext(context)
        // Задание наблюдателя для данного фрагмента (viewModel)
        (context as ResultCurrentViewModelSetter).setResultCurrentViewModel(resultCurrentViewModel)
        // Получение наблюдателя для domain
        publisherDomain = (context as MainActivity).getPublisherDomain()
        // Установка выбранного места (города) как текущего известного места (города)
        // и обновление погодных данных о нём. Теперь при обращении к классу MainChooser он будет
        // выбираться во всех запросах
        publisherDomain.notifyCity(city)
        // Установка навигатора для создания фрагментов с основной информацией приложения (Content)
        navigationContent = (context as MainActivity).getNavigationContent()
        // Получение геттерова и сеттерова на класс MainChooser
        mainChooserGetter = (context as MainActivity).getMainChooserGetter()
        mainChooserSetter = (context as MainActivity).getMainChooserSetter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingReal = FragmentResultCurrentBinding.inflate(inflater, container, false)
        return bindingNotReal.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Создание observer во vieModel
        resultCurrentViewModel.getLiveData().observe(viewLifecycleOwner, Observer<UpdateState> { updateState: UpdateState ->
            renderData(updateState)
        })
        bindingReal?.resultCurrentConstraintLayout?.setOnClickListener(View.OnClickListener {
            // Сброс фильтра места (города)
            with(publisherDomain) {
                notifyDefaultFilterCity("")
                notifyPositionCurrentKnownCity(-1)
            }
            // Отображение фрагмента со списком мест (city) для выбора погоды по другому интересующему месту
            navigationContent?.let{it.showListCitiesFragment(city.country
                    == ConstantsUi.FILTER_RUSSIA, false)}
        })

        // Запуск сервиса GetDataFromInternetService для получения данных о погоде
        val intent = Intent(requireActivity(), GetDataFromInternetService::class.java)
        intent.putExtra(ConstantsUi.LATITUDE_NAME, city.lat)
        intent.putExtra(ConstantsUi.LONGITUDE_NAME, city.lon)
        intent.putExtra(ConstantsUi.MAIN_CHOOSER_GETTER, mainChooserGetter)
        intent.putExtra(ConstantsUi.MAIN_CHOOSER_SETTER, mainChooserSetter)
        requireActivity().startService(intent)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(receiver, IntentFilter(ConstantsUi.WEATHER_DATA_INTENT_FILTER))
    }

    private fun renderData(updateState: UpdateState) {
        when (updateState) {
             is UpdateState.Success -> {
                bindingReal?.resultCurrentConstraintLayoutLoadingLayout?.visibility = View.GONE
                bindingReal?.let{
                    if ((updateState.city != null) && (updateState.dataWeather != null)) {
                        it.resultCurrentConstraintLayoutCityName?.text = updateState.city?.name
                        it.resultCurrentConstraintLayoutCityCoordinates?.text =
                            "${updateState.city?.lat}; ${updateState.city?.lon}"
                        it.resultCurrentConstraintLayoutTemperatureValue?.text =
                            "${updateState.dataWeather?.temperature}"
                        it.resultCurrentConstraintLayoutFeelslikeValue?.text =
                            "${updateState.dataWeather?.feelsLike}"
                    }
                }
                bindingReal?.let {
                    it.root.showSnackBarWithoutAction(it.root, resources.getString(R.string.success), Snackbar.LENGTH_SHORT)
                }
            }
            UpdateState.Loading -> {
                bindingReal?.resultCurrentConstraintLayoutLoadingLayout?.visibility = View.VISIBLE
            }
            is UpdateState.Error -> {
                bindingReal?.resultCurrentConstraintLayoutLoadingLayout?.visibility = View.GONE
                val throwable = updateState.error
                bindingReal?.let {
                    it.root.showSnackBarWithAction(it.root, stickStringsValues()(resources.getString(R.string.error), throwable, resources.getString(R.string.error_no_connection)), resources.getString(R.string.try_another), Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    // Установка SnackBar с действием (В случае ошибки при загрузке погодных данных)
    fun View.showSnackBarWithAction(view: View, messageText: String, actionText: String, showTime: Int) {
        Snackbar.make(view, messageText, showTime).setAction(actionText, View.OnClickListener {
            // Установка выбранного места (города) как текущего известного места (города) и обновление погодных данных о нём. Теперь при обращении к классу MainChooser он будет выбираться во всех запросах
            publisherDomain.notifyCity(city)
        }).show()
    }

    // Пример функции, которая возвращает другую функцию
    fun stickStringsValues() : (errorValue: String, throwable: Throwable?, noConnectionValue: String) -> String {
        return {errorValue, throwable, noConnectionValue ->
            "$errorValue: " + (throwable ?: noConnectionValue)
        }
    }

    // Установка SnackBar без действия (в случае успешной загрузки погодных данных)
    fun View.showSnackBarWithoutAction(view: View, string: String, showTime: Int) {
        Snackbar.make(view, string, showTime).show()
    }

    // Удаление binding при закрытии фрагмента
    override fun onDestroy() {
        super.onDestroy()
        bindingReal = null
    }
}
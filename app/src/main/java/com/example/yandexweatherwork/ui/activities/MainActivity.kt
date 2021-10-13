package com.example.yandexweatherwork.ui.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.controller.navigations.content.NavigationGetterContent
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationGetterDialogs
import com.example.yandexweatherwork.controller.observers.domain.*
import com.example.yandexweatherwork.controller.observers.viewmodels.*
import com.example.yandexweatherwork.domain.core.MainChooser
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserGetterGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetterGetter
import com.example.yandexweatherwork.repository.NetworkChangeBroadcastReceiver
import com.example.yandexweatherwork.repository.facadeuser.RepositoryGetCityCoordinates
import com.example.yandexweatherwork.ui.ConstantsUi
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity:
    AppCompatActivity(),
    ResultCurrentViewModelSetter,
    ListCitiesViewModelSetter,
    PublisherGetterDomain,
    ListSitiesPublisherGetterDomain,
    ObserverDomain,
    NavigationGetterContent,
    NavigationGetterDialogs,
    MainChooserGetterGetter,
    MainChooserSetterGetter {

    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var resultCurrentViewModel: ResultCurrentViewModel = ResultCurrentViewModel()
    private var listCitiesViewModel: ListCitiesViewModel = ListCitiesViewModel()
    private val publisherDomain: PublisherDomain = PublisherDomain()
    private val listCitiesPublisherDomain: ListCitiesPublisherDomain = ListCitiesPublisherDomain()
    private val mainChooser: MainChooser = MainChooser()
    private val mainChooserSetter: MainChooserSetter = MainChooserSetter(mainChooser)
    private val mainChooserGetter: MainChooserGetter = MainChooserGetter(mainChooser)
    private val navigationContent: NavigationContent = NavigationContent(supportFragmentManager,
        mainChooserSetter, mainChooserGetter)
    private val navigationDialogs: NavigationDialogs = NavigationDialogs()
    private val repositoryGetCityCoordinates: RepositoryGetCityCoordinates
    = RepositoryGetCityCoordinates("Москва", mainChooserSetter)
    // Регистрация переменных для анализа связи (CONNECTIVITY_ACTION)
    // СПОСОБ №2:
    private var intentFilter = IntentFilter(ConstantsUi.BROADCAST_ACTION)
    private var receiver: NetworkChangeBroadcastReceiver = NetworkChangeBroadcastReceiver()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Подключение наблюдателей за domain к MainActivity
        publisherDomain.subscribe(this)
        listCitiesPublisherDomain.subscribe(this)

        // Случай первого запуска активити
        if (savedInstanceState == null) {
            // Получение известных городов
            getKnownCities()
            // Выбор места для поиска и отображения погодных данных
            if (mainChooserGetter.getPositionCurrentKnownCity() == -1)
                // Отображение фрагмента со списком мест (city) для выбора интересующего места
                navigationContent.showListCitiesFragment( false)
            else
                // Отображение фрагмента с прогнозом погоды по выбранному ранее месту
                navigationContent.showResultCurrentFragment(mainChooserGetter
                    .getCurrentKnownCity()!!, true, false)
        }

        // Установка AppBarMenu
        setupAppBarMenu()

        //region ПОДПИСКИ НА СОБЫТИЕ ИЗМЕНЕНИЯ ИНТЕРНЕТ-СВЯЗИ (CONNECTIVITY_ACTION)
        // СПОСОБ №1 (результат в тосте и в логе "mylogs"):
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, filter)
        // СПОСОБ №2 (результат в тосте и в логе "mylogs"):
        registerReceiver(receiver, intentFilter)
        //endregion

        // Установка приёмника сообщения о получении нового места
        initNotificationChannel()

        // Установка навигаторов
        navigationContent.setterNavigationDialogs(navigationDialogs)
        navigationDialogs.setterNavigationContent(navigationContent)

        // Установка слушателя push-уведомления от Firebase
        FirebaseMessaging.getInstance().token.addOnCompleteListener { it->
            if(it.isSuccessful){
                Log.d("mylogs",it.result.toString())
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun initNotificationChannel() {
        // Инициализация канала нотификаций
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val importance: Int = NotificationManager.IMPORTANCE_LOW;
            val channel  =  NotificationChannel("2", "name", importance);
            notificationManager.let { channel }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter);
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver);
    }

    //region МЕТОДЫ СПОСОБА №1 ДЛЯ ПОЛУЧЕНИЯ СОБЫТИЯ ИЗМЕНЕНИЯ ИНТЕРНЕТ-СВЯЗИ (CONNECTIVITY_ACTION)
    // Создание BroadcastReceiver для получения события изменения Интернет-связи (CONNECTIVITY_ACTION)
    private var networkStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val noConnectivity =
                intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (!noConnectivity) {
                onConnectionFound()
            } else {
                onConnectionLost()
            }
        }
    }
    fun onConnectionLost() {
        Toast.makeText(this, "СПОСОБ №1: Связь ПОТЕРЯНА (но есть ли Интернет неизвестно)", Toast.LENGTH_LONG).show()
        Log.d("mylogs", "СПОСОБ №1: Связь ПОТЕРЯНА (но есть ли Интернет неизвестно)")
    }
    fun onConnectionFound() {
        Toast.makeText(this, "СПОСОБ №1: Связь ЕСТЬ (но есть ли Интернет неизвестно)", Toast.LENGTH_LONG).show()
        Log.d("mylogs", "СПОСОБ №1: Связь ЕСТЬ (но есть ли Интернет неизвестно)")
    }
    //endregion

    //region ФУНКЦИИ ДЛЯ APPBARMENU
    // Установка меню AppBarMenu
    private fun setupAppBarMenu() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    // Создание меню AppBarMenu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_appbar, menu)
        return true
    }
    // Установка слушателя на меню AppBarMenu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_app_menu_action_add_city -> {
                // Добавить место
                navigationDialogs.showAddCityDialogFragment(this,
                    null, null, null)
                return true
            }
            R.id.action_app_menu_open_weather_history -> {
                // Получить историю погодных данных из базы данных через Room
                navigationContent.showResultWeatherHistoryFragment(false)
                return true
            }
            R.id.action_app_menu_open_contacts -> {
                // Получить контакты
                navigationContent.showContactsFragment(false)
                return true
            }
            R.id.action_app_open_google_map -> {
                // Открыть фрагмент с Google map
                navigationContent.showGoogleMapFragment(false)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    // Установка наблюдателя для обновления данных в ResultCurrentFragment
    override fun setResultCurrentViewModel(viewModel: ResultCurrentViewModel) {
        resultCurrentViewModel = viewModel
    }

    // Установка наблюдателя для обновления данных в ListCitiesFragment
    override fun setListCitiesViewModel(viewModel: ListCitiesViewModel) {
        listCitiesViewModel = viewModel
        listCitiesViewModel.setMainChooserGetter(mainChooserGetter)
    }

    override fun onStop() {
        super.onStop()
        // Сохранение известных городов
        saveKnownCities()
    }

    //region МЕТОДЫ ДЛЯ РАБОТЫ С SHAREDPREFERENCES
    // Сохранение настроек в SharedPreferences
    private fun saveKnownCities() {
        val numberKnownCities = mainChooserGetter.getNumberKnownCites()
        val sharedPreferences: SharedPreferences = getSharedPreferences(ConstantsUi.SHARED_SAVE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(ConstantsUi.SHARED_NUMBER_SAVED_CITIES, numberKnownCities)
        if (numberKnownCities > 0) {
            val nameStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "name$i"}
            val latStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lat$i"}
            val lonStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lone$i"}
            val countryStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "country$i"}
            val knownCities: List<City>? = mainChooserGetter.getKnownCites("","")
            knownCities?.let{
                var index: Int = 0
                it.forEach { element ->
                    editor.putString(nameStringArray[index], element.name)
                    editor.putFloat(latStringArray[index], element.lat.toFloat())
                    editor.putFloat(lonStringArray[index], element.lon.toFloat())
                    editor.putString(countryStringArray[index++], element.country)
                }
            }
        }
        with(editor) {
            putInt(ConstantsUi.SHARED_POSITION_CURRENT_KNOWN_CITY, mainChooserGetter
                .getPositionCurrentKnownCity())
            putString(ConstantsUi.SHARED_DEFAULT_FILTER_CITY, mainChooserGetter
                .getDefaultFilterCity())
            if (mainChooserGetter.getDefaultFilterCountry() == ConstantsUi.FILTER_RUSSIA) {
                putString(
                    ConstantsUi.SHARED_DEFAULT_FILTER_COUNTRY,
                    mainChooserGetter.getDefaultFilterCountry())
            } else {
                putString(
                    ConstantsUi.SHARED_DEFAULT_FILTER_COUNTRY,
                    ConstantsUi.FILTER_NOT_RUSSIA)
            }
            putBoolean(ConstantsUi.SHARED_USER_CORRECTED_CITY_LIST, mainChooserGetter
                .getUserCorrectedCityList())
            apply()
        }
    }

    // Получение настроек из SharedPreferences
    private fun getKnownCities() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(ConstantsUi.SHARED_SAVE, MODE_PRIVATE)
        val numberKnownCities = sharedPreferences.getInt(ConstantsUi.SHARED_NUMBER_SAVED_CITIES, 0)
        if (numberKnownCities > 0) {
            val nameStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "name$i"}
            val latStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lat$i"}
            val lonStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lone$i"}
            val countryStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "country$i"}
            repeat(numberKnownCities) {
                mainChooserSetter.addKnownCities(City(
                    sharedPreferences.getString(nameStringArray[it], ConstantsUi.UNKNOWN_TEXT)!!,
                    sharedPreferences.getFloat(latStringArray[it], ConstantsUi.ZERO_FLOAT).toDouble(),
                    sharedPreferences.getFloat(lonStringArray[it], ConstantsUi.ZERO_FLOAT).toDouble(),
                    sharedPreferences.getString(countryStringArray[it], ConstantsUi.UNKNOWN_TEXT)!!))
            }
        }
        mainChooserSetter.also{
            it.setPositionCurrentKnownCity(sharedPreferences.getInt(ConstantsUi.SHARED_POSITION_CURRENT_KNOWN_CITY, -1))
            it.setDefaultFilterCity(sharedPreferences.getString(ConstantsUi.SHARED_DEFAULT_FILTER_CITY, "")!!)
            it.setDefaultFilterCountry(sharedPreferences.getString(ConstantsUi.SHARED_DEFAULT_FILTER_COUNTRY, "")!!)
            it.setUserCorrectedCityList(sharedPreferences.getBoolean(ConstantsUi.SHARED_USER_CORRECTED_CITY_LIST, false))
        }

        // Установка известных городов по-умолчанию
        if ((mainChooserGetter.getNumberKnownCites() == 0) && !mainChooserGetter.getUserCorrectedCityList()) {
            mainChooserSetter.initKnownCities()
            // Установка фильтра стран по-умолчанию
            mainChooserSetter.setDefaultFilterCountry(ConstantsUi.FILTER_RUSSIA)
        }
    }
    //endregion

    //region МЕТОДЫ ДЛЯ ПЕРЕДАЧИ РЕЗУЛЬТАТОВ ДЕЙСТВИЙ ПОЛЬЗОВАТЕЛЯ ВО ФРАГМЕНТАХ В КЛАСС MainChooser (domain)
    // Создание метода для передачи наблюдателя PublisherDomain для domain во фрагменты
    override fun getPublisherDomain(): PublisherDomain {
        return publisherDomain
    }
    // Создание метода для передачи наблюдателя ListCitiesPublisherDomain для domain во фрагменты
    override fun getListSitiesPublisherDomain(): ListCitiesPublisherDomain {
        return listCitiesPublisherDomain
    }
    override fun updateFilterCountry(filterCountry: String) {
        mainChooserSetter.setDefaultFilterCountry(filterCountry)
    }
    override fun updateFilterCity(filterCity: String) {
        mainChooserSetter.setDefaultFilterCity(filterCity)
    }
    override fun updatePositionCurrentKnownCity(positionCurrentKnownCity: Int) {
        mainChooserSetter.setPositionCurrentKnownCity(positionCurrentKnownCity)
    }

    // Установка наблюдателя для domain (обновление погодных данных)
    override fun updateCity(city: City) {
        mainChooserSetter.setPositionCurrentKnownCity(city.name, city.country)
    }
    //endregion

    //region МЕТОДЫ ДЛЯ НАВИГАЦИИ ПО ФРАГМЕНТАМ
    override fun getNavigationContent(): NavigationContent {
        return navigationContent
    }
    override fun getNavigationDialogs(): NavigationDialogs {
        return navigationDialogs
    }
    //endregion

    //region МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ГЕТТЕРОВА И СЕТТЕРОВ ЯДРА (MainChooser)
    // (mainChooserGetter и mainChooserSetter)
    override fun getMainChooserGetter(): MainChooserGetter {
        return mainChooserGetter
    }
    override fun getMainChooserSetter(): MainChooserSetter {
        return mainChooserSetter
    }
    //endregion

    override fun onBackPressed() {
    }
}
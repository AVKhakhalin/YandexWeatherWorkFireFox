package com.example.yandexweatherwork.ui.fragments.content.domain

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.databinding.FragmentContactsRequestBinding
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal
import android.widget.Toast
import android.R.id
import android.util.Log
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs


// Фрагмент с контент-провайдером для получения контактов
class ContactsRequestFragment: Fragment() {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var _binding: FragmentContactsRequestBinding? = null
    private val binding: FragmentContactsRequestBinding
        get() {
            return _binding!!
        }
    private var navigationContent: NavigationContent? = null
    private var family: String = ""
    private var name: String = ""
    private var patronymic: String = ""
    private var foundedCity: MutableList<String> = mutableListOf()
    private var streetMaxLenght: String = ""
    private var cityMaxLenght: String = ""
    private var stateMaxLenght: String = ""
    private var countryMaxLenght: String = ""

    private var navigationDialogs: NavigationDialogs? = null
    //endregion

    companion object {
        fun newInstance() = ContactsRequestFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получение навигатора для загрузки фрагментов с основным содержанием приложения (Content)
        navigationContent = (context as MainActivity).getNavigationContent()
        // Получение навигатора для загрузки диалоговых фрагментов (Dialogs)
        navigationDialogs = (context as MainActivity).getNavigationDialogs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Результат нажатия на кнопку "Получить данные"
        binding.fragmentContactsRequestButtonOk.setOnClickListener {
            family = binding.fragmentContactsRequestFamilyField.text.toString()
            name = binding.fragmentContactsRequestNameField.text.toString()
            patronymic = binding.fragmentContactsRequestPatronymicField.text.toString()
            if (name.isNotEmpty()) {
                // Считать данные из адресной книги
                checkPermission()
            } else {
                Snackbar.make(binding.root, "${resources.getString(R.string.error)}: " +
                        "${resources.getString(R.string.error_empty_name_in_contacts_request)}",
                    Snackbar.LENGTH_LONG).show()
            }
        }

        // Результат нажатия на кнопку "Отмена"
        binding.fragmentContactsRequestButtonCancel.setOnClickListener {
            navigationContent?.let { it.showListCitiesFragment(false) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkPermission(){
        context?.let {
            if (ContextCompat.checkSelfPermission(it, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
                // Получить данные о месте из базы контактов
                getFoundedCities()
            }else if(shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder(it)
                    .setTitle("Доступ к контактам")
                    .setMessage("Для получения адреса запрошенного лица нужно разрешить доступ " +
                            "к контактам на телефоне")
                    .setPositiveButton("Разрешаю"){ dialog, which ->
                        myRequestPermission()
                    }
                    .setNegativeButton("Не разрешаю"){ dialog, which ->
                        dialog.dismiss()
                    }
                    .create().show()
            }else{
                myRequestPermission()
            }
        }
    }

    private val REQUEST_CODE = 999

    private fun myRequestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE -> {
                if((grantResults.isNotEmpty()) &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Получить данные о месте из базы контактов
                    getFoundedCities()
                }else{
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Повторный запрос на доступ к контактам")
                            .setMessage("Получение разрешения на доступ прилжоению к контактам " +
                                    "позволит приложению самому дать прогноз погоды по адресу " +
                                    "указанного лица (по ФИО). Если, конечно, " +
                                    "его адрес есть в контактах.")
                            .setPositiveButton("Да, разрешаю") { dialog, which ->
                                myRequestPermission()
                            }
                            .setNegativeButton("Нет и больше не спрашивать") { dialog, which ->
                                dialog.dismiss()
                            }
                            .create().show()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Получение данных из базы контактов
    private fun getFoundedCities() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cur = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cur != null) && (cur.count > 0)) {
            var foundedIndex: Int = -1
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val curName = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                // Способ отбора данных по наличию телефонного номера
//                if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
//                        .toInt() > 0
//                ) {
                // Способ отбора данных по ФИО
//                foundedIndex++
//                if ((curName.lowercase().indexOf(family.lowercase()) > -1)
//                    && (curName.lowercase().indexOf(name.lowercase()) > -1)
//                    && (curName.lowercase().indexOf(patronymic.lowercase()) > -1)) {
//                    Log.d("mylogs", "Имя: $curName, ID: $id")

                    // Получение телефонного номера
                    val pCur = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val phone = pCur.getString(
                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                        // Вывод полученной информации
//                        Log.d("mylogs", "Телефонный номер: $phone")
                    }
                    pCur.close()

                    // Получение адресов электронной почты и их типа
                    val emailCur = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (emailCur!!.moveToNext()) {
                        val email = emailCur.getString(
                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                        )
                        val emailType = emailCur.getString(
                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)
                        )
                        // Вывод полученной информации
//                        Log.d("mylogs", "Email and type: $email, $emailType")
                    }
                    emailCur.close()

                    // Получение заметок
                    val noteWhere =
                        ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data
                            .MIMETYPE + " = ?"
                    val noteWhereParams = arrayOf(
                        id,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
                    )
                    val noteCur = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        noteWhere,
                        noteWhereParams,
                        null
                    )
                    if (noteCur!!.moveToFirst()) {
                        val note =
                            noteCur.getString(noteCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Note.NOTE))
                        // Вывод полученной информации
//                        Log.d("mylogs", "Note: $note")
                    }
                    noteCur.close()

                    // Получение данных об адресе
                    val addrWhere =
                        ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                ContactsContract.Data.MIMETYPE + " = ?"
                    val addrWhereParams = arrayOf(
                        id,
                        StructuredPostal.CONTENT_ITEM_TYPE
                    )
                    val addrCur = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null, null, null, null
                    )
//                    var curIndex: Int = -1
//                    while (addrCur!!.moveToNext()) {
                    while (addrCur!!.moveToNext()) {
                        val poBox = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.POBOX)
                        )
                        val street = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.STREET)
                        )
                        val city = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.CITY)
                        )
                        val state = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.REGION)
                        )
                        val postalCode = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.POSTCODE)
                        )
                        val country = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.COUNTRY)
                        )
                        val type = addrCur.getString(
                            addrCur.getColumnIndex(StructuredPostal.TYPE)
                        )
                        // Вывод полученной информации

/*                        Log.d("mylogs", "Postal Address: Общая информация: $addrWhere, " +
                                "Массив данных: $addrWhereParams" +
                                "Почтовый ящик: $poBox, Улица: $street, " +
                                "Город: $city, Страна: $state, Почтовый код: $postalCode, " +
                                "Страна: $country, Тип: $type")*/
//                        curIndex++
//                        if (curIndex == foundedIndex) {

                            var addressFIOName = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            if ((addressFIOName.lowercase().indexOf(family.lowercase()) > -1)
                            && (addressFIOName.lowercase().indexOf(name.lowercase()) > -1)
                            && (addressFIOName.lowercase().indexOf(patronymic.lowercase()) > -1)) {
//                                Log.d("mylogs", "FIO name: $addressFIOName")

/*                                if (street != null) {
                                    Log.d("myLogs", "Улица: $street")
                                }
                                if (city != null) {
                                    Log.d("myLogs", "Город: $city")
                                }
                                if (state != null) {
                                    Log.d("myLogs", "Область: $state")
                                }
                                if (country != null) {
                                    Log.d("myLogs", "Страна: $country")
                                }*/

                                // Выявление самых длинных записей в категориях "Улица", "Город",
                                // "Область", "Страна"
//                                if ((street != null) && (street.length > streetMaxLenght.length)) {
                                if (street != null) {
                                    streetMaxLenght = street
                                    val indexOfSpace: Int = streetMaxLenght.indexOf(" ")
                                    if (indexOfSpace > -1) {
                                        streetMaxLenght = streetMaxLenght.substring(0, indexOfSpace)
                                    }
                                }
//                                if ((city != null) && (city.length > cityMaxLenght.length)) {
                                if (city != null) {
                                    cityMaxLenght = city
                                    val indexOfSpace: Int = cityMaxLenght.indexOf(" ")
                                    if (indexOfSpace > -1) {
                                        cityMaxLenght = cityMaxLenght.substring(0, indexOfSpace)
                                    }
                                }
//                                if ((state != null) && (state.length > stateMaxLenght.length)) {
                                if (state != null) {
                                    stateMaxLenght = state
                                    val indexOfSpace: Int = stateMaxLenght.indexOf(" ")
                                    if (indexOfSpace > -1) {
                                        stateMaxLenght = stateMaxLenght.substring(0, indexOfSpace)
                                    }
                                }
//                                if ((country != null) &&
//                                (country.length > countryMaxLenght.length)) {
                                if (country != null) {
                                    countryMaxLenght = country
                                    val indexOfSpace: Int = countryMaxLenght.indexOf(" ")
                                    if (indexOfSpace > -1) {
                                        countryMaxLenght =
                                            countryMaxLenght.substring(0, indexOfSpace)
                                    }
                                }

                                // Занесение данных об адресе в список foundedCities
                                addNewAdressToFoundedCities()
                            }
//                        }
                    }
                    addrCur.close()

                    // Получение данных об "Instant Messenger"
                    val imWhere =
                        ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                ContactsContract.Data.MIMETYPE + " = ?"
                    val imWhereParams = arrayOf(
                        id,
                        ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE
                    )
                    val imCur = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null, imWhere, imWhereParams, null
                    )
                    if (imCur!!.moveToFirst()) {
                        val imName = imCur.getString(
                            imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)
                        )
                        var imType: String?
                        imType = imCur.getString(
                            imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE)
                        )
                        // Вывод полученной информации
//                        Log.d("mylogs", "Instant Messenger: $imName, $imType")
                    }
                    imCur.close()

                    // Получение данных об организации
                    val orgWhere =
                        ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                ContactsContract.Data.MIMETYPE + " = ?"
                    val orgWhereParams = arrayOf(
                        id,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                    )
                    val orgCur = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null
                    )
                    if (orgCur!!.moveToFirst()) {
                        val orgName =
                            orgCur.getString(orgCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.DATA))
                        val title =
                            orgCur.getString(orgCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Organization.TITLE))
                        // Вывод полученной информации
//                        Log.d("mylogs", "Organizations: $orgName, $title")
                    }
                    orgCur.close()
//                }
                // Условие одинарного вхождения в цикл
                break
            }
        }

        // Передача полученных данных
//        Log.d("mylogsRes", "$foundedCity")
        navigationDialogs?.let {
            it.showListContactFoundedCitiesDialogFragment(foundedCity, requireActivity())
        }

        // Обнуление данных
        foundedCity.clear()
        streetMaxLenght = ""
        cityMaxLenght = ""
        stateMaxLenght = ""
        countryMaxLenght = ""
    }

    private fun addNewAdressToFoundedCities() {
        // Проверка новых элементов на наличие в них только "+", "(", ")" и цифр
        streetMaxLenght = if(isPosOrNegNumber(streetMaxLenght)) "" else streetMaxLenght
        cityMaxLenght = if(isPosOrNegNumber(cityMaxLenght)) "" else cityMaxLenght
        stateMaxLenght = if(isPosOrNegNumber(stateMaxLenght)) "" else stateMaxLenght
        countryMaxLenght = if(isPosOrNegNumber(countryMaxLenght)) "" else countryMaxLenght

        var lenStreet: Int = streetMaxLenght?.length ?: 0
        var lenCity: Int = cityMaxLenght?.length ?: 0
        var lenState: Int = stateMaxLenght?.length ?: 0
        var lenCountry: Int = countryMaxLenght?.length ?: 0

//        val arrayOfLengths: Array<Int> = arrayOf(lenStreet, lenCity, lenState, lenCountry)
//        var maxLenght: Int = lenStreet
//        var indexMaxLenght: Int = 0
        if ((lenStreet != 0) || (lenCity != 0) || (lenState != 0) || (lenCountry != 0)) {
//            arrayOfLengths.forEachIndexed() { index, lenght ->
//                if ((index != 0) && (maxLenght < lenght)) {
//                    maxLenght = lenght
//                    indexMaxLenght = index
//                }
//            }
////            when (indexMaxLenght) {
////                    0 -> streetMaxLenght?.let { foundedCities.add(it) }
////                    1 -> cityMaxLenght?.let { foundedCities.add(it) }
////                    2 -> stateMaxLenght?.let { foundedCities.add(it) }
////                    3 -> countryMaxLenght?.let { foundedCities.add(it) }
////            }
//            var resFoundedCities = ""
//            if (lenCity > 1) cityMaxLenght?.let { resFoundedCities += cityMaxLenght }
//            if (lenStreet > 1) streetMaxLenght?.let { resFoundedCities += if (resFoundedCities.length == 0) "$streetMaxLenght" else ", $streetMaxLenght" }
//            if (lenState > 1) stateMaxLenght?.let { resFoundedCities += if (resFoundedCities.length == 0) "$stateMaxLenght" else ", $stateMaxLenght" }
//            if (lenCountry > 1) countryMaxLenght?.let { resFoundedCities += if (resFoundedCities.length == 0) "$countryMaxLenght" else ", $countryMaxLenght" }
//            foundedCity.add(resFoundedCities)
//            cityMaxLenght = ""
//            streetMaxLenght = ""
//            stateMaxLenght = ""
//            countryMaxLenght = ""

            // Добавление улицы
            if (lenStreet > 1) streetMaxLenght?.let {
                var isOriginalNewData: Boolean = true
                foundedCity.forEach() {
                    if (it == streetMaxLenght) {
                        isOriginalNewData = false
                    }
                }
                if (isOriginalNewData) foundedCity.add(streetMaxLenght)
            }

            // Добавление города
            if (lenCity > 1) cityMaxLenght?.let {
                var isOriginalNewData: Boolean = true
                foundedCity.forEach() {
                    if (it == cityMaxLenght) {
                        isOriginalNewData = false
                    }
                }
                if (isOriginalNewData) foundedCity.add(cityMaxLenght)
            }

            // Добавление района
            if (lenState > 1) stateMaxLenght?.let {
                var isOriginalNewData: Boolean = true
                foundedCity.forEach() {
                    if (it == stateMaxLenght) {
                        isOriginalNewData = false
                    }
                }
                if (isOriginalNewData) foundedCity.add(stateMaxLenght)
            }

            // Добавление страны
            if (lenCountry > 1) countryMaxLenght?.let {
                var isOriginalNewData: Boolean = true
                foundedCity.forEach() {
                    if (it == countryMaxLenght) {
                        isOriginalNewData = false
                    }
                }
                if (isOriginalNewData) foundedCity.add(countryMaxLenght)
            }
        }
    }

    // Проверка на наличие в строке символа "+", "(", ")" и цифр
    fun isPosOrNegNumber(inputedString: String?): Boolean {
//        val regex = """^(-)?[0-9]{0,}((\.){1}[0-9]{1,}){0,1}$""".toRegex()
        val regex = """^((8|\+7)[\- ]?)?(\(?\d{3}\)?[\- ]?)?[\d\- ]{7,10}${'$'}""".toRegex()
        return if (inputedString.isNullOrEmpty()) false
        else regex.matches(inputedString)
    }
}
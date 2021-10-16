package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import androidx.fragment.app.DialogFragment
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.ui.ConstantsUi
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class CardCityDialogFragment(
    private val positionChoosedElement: Int,
    private var city: City,
    private val listCitiesFragment: ListCitiesFragment
): DialogFragment(), DialogInterface.OnClickListener {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var buttonYes: Button? = null
    private var buttonNo: Button? = null
    private var inputCityNameField: EditText? = null
    private var inputLatField: EditText? = null
    private var inputLonField: EditText? = null
    private var cardCountryField: EditText? = null
    private var switchRusNotRus: Switch? = null
    private var imageRussia: ImageView? = null
    private var imageNotRussia: ImageView? = null
    private var countryName: String =
        if ((city.country == null) || (city.country.length == 0))
            ConstantsUi.NOT_RUSSIA_NAME else city.country
    // Задание интерфеса для ввода координат (только числа, десятичная точка и минус)
    val inputTypeCoordinatesInterface = InputType.TYPE_CLASS_NUMBER or // разрешить ввод числа
            InputType.TYPE_NUMBER_FLAG_DECIMAL or // разрешить ввод десятичной точки
            InputType.TYPE_NUMBER_FLAG_SIGNED // разрешить ввод положительных и отрицательных чисел
    // endregion

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_card_city, null)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        inputCityNameField = view.findViewById(R.id.input_city_info_city_field)
        inputCityNameField?.let{it.setText("${city.name}")}
        inputLatField = view.findViewById(R.id.input_city_info_lat_field)
        inputLatField?.let{
            it.inputType = inputTypeCoordinatesInterface
            it.setText("${city.lat}")
        }
        inputLonField = view.findViewById(R.id.input_city_info_lon_field)
        inputLonField?.let{
            it.inputType = inputTypeCoordinatesInterface
            it.setText("${city.lon}")
        }
        cardCountryField = view.findViewById(R.id.input_city_info_country_field)

        switchRusNotRus = view.findViewById(R.id.card_city_info_country_rus_notrus_switch)
        imageRussia = view.findViewById(R.id.card_city_info_country_russian_button)
        imageNotRussia = view.findViewById(R.id.card_city_info_country_notrussian_button)

        // Установка события нажатия на позитивную кнопку
        buttonYes = view.findViewById(R.id.edit_city_info_button_ok)
        if (buttonYes != null) {
            buttonYes!!.setOnClickListener(View.OnClickListener { view: View ->
                onYes(view)
            })
        }

        // Установка события нажатия на негативную кнопку
        buttonNo = view.findViewById(R.id.edit_city_info_button_cancel)
        if (buttonNo != null) {
            buttonNo!!.setOnClickListener(View.OnClickListener { view: View ->
                onNo(view)
            })
        }

        // Установка нажатия на переключатель названий стран
        if ((switchRusNotRus != null) && (imageRussia != null) && (imageNotRussia != null) &&
            (cardCountryField != null)) {
            switchRusNotRus!!.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    imageRussia!!.setImageResource(R.drawable.ic_russia_gray)
                    imageNotRussia!!.setImageResource(R.drawable.ic_earth)
                    checkAndSaveNotRussianCountryName()
                    if (countryName == ConstantsUi.FILTER_RUSSIA) {
                        cardCountryField!!.setText(ConstantsUi.NOT_RUSSIA_NAME)
                    } else {
                        cardCountryField!!.setText(countryName)
                    }
                } else {
                    imageRussia!!.setImageResource(R.drawable.ic_russia)
                    imageNotRussia!!.setImageResource(R.drawable.ic_earth_gray)
                    checkAndSaveNotRussianCountryName()
                    cardCountryField!!.setText(ConstantsUi.FILTER_RUSSIA)
                }
            }
        }

        // Настройка поля с названием страны и переключателя по текущим данным в city.name
        cardCountryField?.let{it.setText(countryName)}
        if (countryName != ConstantsUi.FILTER_RUSSIA) {
            imageRussia?.let { it.setImageResource(R.drawable.ic_russia_gray) }
            imageNotRussia?.let { it.setImageResource(R.drawable.ic_earth) }
            switchRusNotRus?.let { it.isChecked = true }
        }

        // Установка события нажатия на логотип "Россия"
        if ((imageRussia != null) && (switchRusNotRus != null)) {
            imageRussia!!.setOnClickListener {
                if (switchRusNotRus!!.isChecked) {
                    imageRussia!!.setImageResource(R.drawable.ic_russia)
                    imageNotRussia!!.setImageResource(R.drawable.ic_earth_gray)
                    checkAndSaveNotRussianCountryName()
                    cardCountryField!!.setText(ConstantsUi.FILTER_RUSSIA)
                    switchRusNotRus!!.isChecked = false
                }
            }
        }

        // Установка события нажатия на логотип "Не России"
        if ((imageNotRussia != null) && (switchRusNotRus != null)) {
            imageNotRussia!!.setOnClickListener {
                if (!switchRusNotRus!!.isChecked) {
                    imageRussia!!.setImageResource(R.drawable.ic_russia_gray)
                    imageNotRussia!!.setImageResource(R.drawable.ic_earth)
                    checkAndSaveNotRussianCountryName()
                    cardCountryField!!.setText(countryName)
                    switchRusNotRus!!.isChecked = true
                }
            }
        }
    }

    // Проверка и сохранение введённого названия иностранной страны
    private fun checkAndSaveNotRussianCountryName() {
        if ((cardCountryField!!.text != null) &&
            (cardCountryField!!.text.toString().lowercase() !=
                    ConstantsUi.FILTER_RUSSIA.lowercase()) &&
            (cardCountryField!!.text.toString() != ConstantsUi.NOT_RUSSIA_NAME)
        ) {
            countryName = cardCountryField!!.text.toString()
        }
    }

    // Результат нажатия на кнопку отмены действия
    private fun onNo(view: View) {
        dismiss()
    }

    // Результат нажатия на кнопку подтверждения действия
    private fun onYes(view: View) {
        if ((inputCityNameField != null) && (inputLatField != null)
            && (inputLonField  != null) && (cardCountryField != null))
                if (inputLatField!!.text.isNotEmpty() && inputLonField!!.text.isNotEmpty()) {
                    listCitiesFragment.editCitiesAndUpdateList(
                        positionChoosedElement,
                        City("${inputCityNameField!!.text}",
                            "${inputLatField!!.text}".toDouble(),
                            "${inputLonField!!.text}".toDouble(),
                            "${cardCountryField!!.text}"
                        )
                    )
                } else {
                    listCitiesFragment.editCitiesAndUpdateList(
                        positionChoosedElement,
                        City(
                            "${inputCityNameField!!.text}",
                            ConstantsUi.ERROR_COORDINATE,
                            ConstantsUi.ERROR_COORDINATE,
                            "${cardCountryField!!.text}"
                        )
                    )
                }
        dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {}
}
package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.ui.ConstantsUi
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class InputCityDialogFragment(
    private val listCitiesFragment: ListCitiesFragment
): DialogFragment(), DialogInterface.OnClickListener {
    private var buttonYes: Button? = null
    private var buttonNo: Button? = null
    private var inputCityNameField: EditText? = null
    private var inputLatField: EditText? = null
    private var inputLonField: EditText? = null
    private var inputCountryField: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_input_city, null)
/*        initView(view)
        inputCityNameField = view.findViewById(R.id.input_city_info_city_field)
        inputLatField = view.findViewById(R.id.input_city_info_lat_field)
        inputLonField = view.findViewById(R.id.input_city_info_lon_field)
        inputCountryField = view.findViewById(R.id.input_city_info_country_field)*/
        return view
    }

/*    private fun initView(view: View) {
        buttonYes = view.findViewById(R.id.edit_city_info_button_ok)
        if (buttonYes != null) {
            buttonYes!!.setOnClickListener(View.OnClickListener { view: View ->
                onYes(view)
            })
        }
        buttonNo = view.findViewById(R.id.edit_city_info_button_cancel)
        if (buttonNo != null) {
            buttonNo!!.setOnClickListener(View.OnClickListener { view: View ->
                onNo(view)
            })
        }
    }*/

    // Результат нажатия на кнопку отмены действия
    private fun onNo(view: View) {
        dismiss()
    }

    // Результат нажатия на кнопку подтверждения действия
/*    private fun onYes(view: View) {
        if ((inputCityNameField != null) && (inputLatField != null)
            && (inputLonField  != null) && (inputCountryField != null))
                if (inputLatField!!.text.isNotEmpty() && inputLonField!!.text.isNotEmpty()) {
                    listCitiesFragment.editCitiesAndUpdateList(
                        City("${inputCityNameField!!.text}",
                            "${inputLatField!!.text}".toDouble(),
                            "${inputLonField!!.text}".toDouble(),
                            "${inputCountryField!!.text}"
                        )
                    )
                } else {
                    listCitiesFragment.editCitiesAndUpdateList(
                        City(
                            "${inputCityNameField!!.text}",
                            ConstantsUi.ERROR_COORDINATE,
                            ConstantsUi.ERROR_COORDINATE,
                            "${inputCountryField!!.text}"
                        )
                    )
                }
        dismiss()
    }*/

    override fun onClick(dialog: DialogInterface?, which: Int) {}
}
package com.example.yandexweatherwork.ui.fragments.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModel
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment

class DeleteConformationDialogFragment(
    private val positionChoosedElement: Int,
    private var filterCity: String,
    private var filterCountry: String,
    private val listCitiesFragment: ListCitiesFragment
): DialogFragment(), DialogInterface.OnClickListener {
    private var buttonYes: Button? = null
    private var buttonNo: Button? = null
    private var titleTextDialogFragment: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_delete_conformation, null)
        initView(view)
        titleTextDialogFragment = view.findViewById(R.id.dialog_fragment_delete_conformation_title)
        titleTextDialogFragment?.let{
            it.text = "${it.text}\"$filterCity\""
        }
        return view
    }

    private fun initView(view: View) {
        buttonYes = view.findViewById(R.id.way_input_button_ok)
        if (buttonYes != null) {
            buttonYes!!.setOnClickListener(View.OnClickListener { view: View ->
                onYes(view)
            })
        }
        buttonNo = view.findViewById(R.id.way_input_button_cancel)
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
    fun onYes(v: View) {
        listCitiesFragment.deleteCitiesAndUpdateList(positionChoosedElement, filterCity, filterCountry)
        Toast.makeText(context, "$filterCity; $filterCountry", Toast.LENGTH_LONG).show()
        dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {}
}

enum class DeleteAnswersTypes {
    // Значение НЕТ (не удалять место)
    NO,
    // Значение ДА (удалять место)
    YES
}
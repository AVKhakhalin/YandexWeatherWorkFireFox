package com.example.yandexweatherwork.controller.navigations.dialogs

import androidx.fragment.app.FragmentActivity
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.CardCityDialogFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.DeleteConformationDialogFragment

class NavigationDialogs{
    // Отображение диалога с подтверждением удаления фрагмента
    fun showDeleteConformationDialogFragment(positionChoosedElement: Int,
                                             filterCity: String,
                                             filterCountry: String,
                                             listCitiesFragment: ListCitiesFragment,
                                             fragmentActivity: FragmentActivity) {
        val deleteConformationDialogFragment: DeleteConformationDialogFragment
                = DeleteConformationDialogFragment(positionChoosedElement,
            filterCity, filterCountry, listCitiesFragment)
        deleteConformationDialogFragment.show(fragmentActivity.supportFragmentManager, "")
    }

    // Отображение диалога с карточкой места (города)
    fun showCardCityDialogFragment(positionChoosedElement: Int,
                                   city: City,
                                   listCitiesFragment: ListCitiesFragment,
                                   fragmentActivity: FragmentActivity) {
        val cardCityDialogFragment: CardCityDialogFragment
                = CardCityDialogFragment(positionChoosedElement,
            city, listCitiesFragment)
        cardCityDialogFragment.show(fragmentActivity.supportFragmentManager, "")
    }
}
package com.example.yandexweatherwork.controller.navigations.dialogs

import androidx.fragment.app.FragmentActivity
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.CardCityDialogFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.DeleteConformationDialogFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.AddCityDialogFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.ListFoundedCitiesDialogFragment

class NavigationDialogs{
    private var listCitiesFragment: ListCitiesFragment? = null

    // Установка класса ListCitiesFragment
    fun setListCitiesFragment(listCitiesFragment: ListCitiesFragment) {
        this.listCitiesFragment = listCitiesFragment
    }

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
        // Сохранение класса listCitiesFragment
        this.listCitiesFragment = listCitiesFragment
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
        // Сохранение класса listCitiesFragment
        this.listCitiesFragment = listCitiesFragment
    }

    // Отображение диалога с карточкой места (города)
    fun showAddCityDialogFragment(fragmentActivity: FragmentActivity) {
        listCitiesFragment?.let {
            val addCityDialogFragment: AddCityDialogFragment
            = AddCityDialogFragment(listCitiesFragment!!)
            addCityDialogFragment.show(fragmentActivity.supportFragmentManager, "")
        }
    }

    // Отображение диалога со списком найденных новых мест (городов)
    fun showListFoundedCitiesDialogFragment(newCitiesInfoFiltred: MutableList<CityDTO>?,
                                            fragmentActivity: FragmentActivity) {
        listCitiesFragment?.let {
            val listFoundedCitiesDialogFragment: ListFoundedCitiesDialogFragment
            = ListFoundedCitiesDialogFragment(newCitiesInfoFiltred, listCitiesFragment!!)
            listFoundedCitiesDialogFragment.show(fragmentActivity.supportFragmentManager, "")
        }
    }
}
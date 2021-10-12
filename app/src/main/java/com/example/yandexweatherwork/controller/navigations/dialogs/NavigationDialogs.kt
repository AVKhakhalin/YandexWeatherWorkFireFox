package com.example.yandexweatherwork.controller.navigations.dialogs

import androidx.fragment.app.FragmentActivity
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.CityDTO
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.content.domain.MapsFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.*

class NavigationDialogs{
    private var listCitiesFragment: ListCitiesFragment? = null
    private var mapsFragment: MapsFragment? = null
    private var navigationContent: NavigationContent? = null

    fun setterNavigationContent(navigationContent: NavigationContent) {
        this.navigationContent = navigationContent
    }
    fun getterNavigationContent(): NavigationContent? {
        return navigationContent
    }

    fun setterMapsFragment(mapsFragment: MapsFragment?) {
        this.mapsFragment = mapsFragment
    }
    fun getterMapsFragment(): MapsFragment? {
        return mapsFragment
    }

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

    // Отображение диалога с добавлением нового места (города)
    fun showAddCityDialogFragment(fragmentActivity: FragmentActivity, defaultPlace: String?,
                                  defaultLatitude: Double?, defaultLongitude: Double?) {
        listCitiesFragment?.let {
            val addCityDialogFragment: AddCityDialogFragment
                    = AddCityDialogFragment(listCitiesFragment!!, defaultPlace,
                defaultLatitude, defaultLongitude)
            addCityDialogFragment.show(fragmentActivity.supportFragmentManager, "")
        }
    }

    // Отображение диалога со списком найденных новых мест (городов)
    fun showListFoundedCitiesDialogFragment(newCitiesInfoFiltred: MutableList<CityDTO>?,
                                            fragmentActivity: FragmentActivity) {
        listCitiesFragment?.let {
            val listFoundedCitiesDialogFragment: ListFoundedCitiesDialogFragment
            = ListFoundedCitiesDialogFragment(newCitiesInfoFiltred, listCitiesFragment!!, this)
            listFoundedCitiesDialogFragment.show(fragmentActivity.supportFragmentManager, "")
        }
    }

    // Отображение диалога со списком найденных в контактах новых мест
    fun showListContactFoundedCitiesDialogFragment(newContactCitiesInfoFiltred:
                                                   MutableList<String>?,
                                                   fragmentActivity: FragmentActivity) {
        // Сохранение полученных мест из контактов в другую переменную,
        // чтобы они не обнулились при их очистке
        var contactCitiesInfoFiltredToSend: MutableList<String> = mutableListOf()
        newContactCitiesInfoFiltred?.let {
            it.forEach() { contactCity ->
                contactCitiesInfoFiltredToSend.add(contactCity)
            }
        }
        // Создание фрагмента для вывода полученных мест из базы контактов
        listCitiesFragment?.let {
            val listContactFoundedCitiesDialogFragment: ListContactFoundedCitiesDialogFragment
                    = ListContactFoundedCitiesDialogFragment(contactCitiesInfoFiltredToSend,
                this)
            listContactFoundedCitiesDialogFragment.show(
                fragmentActivity.supportFragmentManager, "")
        }
    }
}
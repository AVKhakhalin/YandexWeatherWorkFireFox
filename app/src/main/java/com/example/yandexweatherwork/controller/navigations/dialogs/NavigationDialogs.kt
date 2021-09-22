package com.example.yandexweatherwork.controller.navigations.dialogs

import androidx.fragment.app.FragmentActivity
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.dialogs.DeleteConformationDialogFragment

class NavigationDialogs{
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
}
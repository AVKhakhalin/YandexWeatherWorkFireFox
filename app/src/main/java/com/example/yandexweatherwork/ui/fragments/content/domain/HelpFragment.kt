package com.example.yandexweatherwork.ui.fragments.content.domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.databinding.FragmentHelpBinding
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter

class HelpFragment(
    private val navigationContent: NavigationContent
): Fragment() {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var _binding: FragmentHelpBinding? = null
    private val binding: FragmentHelpBinding
        get() {
            return _binding!!
        }
    //endregion

    companion object {
        fun newInstance(navigationContent: NavigationContent) = HelpFragment(navigationContent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        binding.fragmentHelpTextSixth.setOnClickListener {
            navigationContent.showListCitiesFragment(false)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
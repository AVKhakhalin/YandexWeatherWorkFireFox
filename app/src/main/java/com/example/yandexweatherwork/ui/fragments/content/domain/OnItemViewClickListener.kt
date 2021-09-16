package com.example.yandexweatherwork.ui.fragments.content.domain

import com.example.yandexweatherwork.domain.data.City

interface OnItemViewClickListener {
    fun onItemClick(city: City)
}
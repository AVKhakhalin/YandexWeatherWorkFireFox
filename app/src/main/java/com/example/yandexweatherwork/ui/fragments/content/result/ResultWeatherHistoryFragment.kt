package com.example.yandexweatherwork.ui.fragments.content.result

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.controller.observers.viewmodels.UpdateState
import com.example.yandexweatherwork.controller.observers.viewmodels.WeatherHistoryViewModel
import com.example.yandexweatherwork.databinding.FragmentResultWeatherHistoryBinding
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar

class ResultWeatherHistoryFragment: Fragment() {
    private var navigationContent: NavigationContent? = null
    private var listUniqueCities: List<String> = listOf()
    private var _binding: FragmentResultWeatherHistoryBinding? = null
    private val binding: FragmentResultWeatherHistoryBinding
        get() {
            return _binding!!
        }
    private val adapter: ResultWeatherHistoryFragmentAdapter by lazy {
        ResultWeatherHistoryFragmentAdapter(this, navigationContent)
    }

    private val viewModel: WeatherHistoryViewModel by lazy {
        ViewModelProvider(this).get(WeatherHistoryViewModel::class.java)
    }

    fun getFragmentViewModel(): WeatherHistoryViewModel = viewModel

    companion object {
        fun newInstance() = ResultWeatherHistoryFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Установка навигатора для создания фрагментов с основной информацией приложения (Content)
        navigationContent = (context as MainActivity).getNavigationContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultWeatherHistoryBinding.inflate(inflater, container, false)
        binding.resultWeatherHistoryCancel.setOnClickListener{
            navigationContent?.let {it.showListCitiesFragment(false)}
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
//        viewModel.getAllHistory()
        viewModel.getUniqueCitiesNames()
    }

    private fun renderData(updateState: UpdateState) {
        when (updateState) {
            is UpdateState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                val throwable = updateState.error
                Snackbar.make(binding.root, "${R.string.success_load_from_database}: $throwable", Snackbar.LENGTH_LONG).show()
            }
            UpdateState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is UpdateState.SuccessGetUniqueCitiesWithWeatherHistory -> {
                binding.weatherHistoryFragmentRecyclerView.adapter = adapter
                binding.loadingLayout.visibility = View.GONE
//                val dataWeather = updateState.weatherData
                listUniqueCities = updateState.listUniqueCities
//                adapter.setWeather(dataWeather)
                adapter.setUniqueListCities(listUniqueCities)
                Snackbar.make(binding.root, resources.getString(R.string.success_load_from_database), Snackbar.LENGTH_LONG).show()

                // Переопределение кнопки Cancel
                binding.resultWeatherHistoryCancel.setOnClickListener{
                    navigationContent?.let {it.showListCitiesFragment(false)}
                }
            }
            is UpdateState.SuccessGetCityWeatherHistory -> {
                binding.weatherHistoryFragmentRecyclerView.adapter = adapter
                binding.loadingLayout.visibility = View.GONE
                val dataWeather = updateState.weatherData
                adapter.setWeather(dataWeather)
                Snackbar.make(binding.root, resources.getString(R.string.success_load_from_database), Snackbar.LENGTH_LONG).show()

                // Переопределение кнопки Cancel
                binding.resultWeatherHistoryCancel.setOnClickListener{
                    adapter.setUniqueListCities(listUniqueCities)
                    Snackbar.make(binding.root, resources.getString(R.string.success_load_from_database), Snackbar.LENGTH_LONG).show()
                    binding.resultWeatherHistoryCancel.setOnClickListener{
                        navigationContent?.let {it.showListCitiesFragment(false)}
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
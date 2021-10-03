package com.example.yandexweatherwork.ui.fragments.content.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.observers.viewmodels.UpdateState
import com.example.yandexweatherwork.controller.observers.viewmodels.WeatherHistoryViewModel
import com.example.yandexweatherwork.databinding.FragmentResultWeatherHistoryBinding
import com.google.android.material.snackbar.Snackbar

class ResultWeatherHistoryFragment: Fragment() {
    private var _binding: FragmentResultWeatherHistoryBinding? = null
    private val binding: FragmentResultWeatherHistoryBinding
        get() {
            return _binding!!
        }
    private val adapter: ResultWeatherHistoryFragmentAdapter by lazy {
        ResultWeatherHistoryFragmentAdapter()
    }

    private val viewModel: WeatherHistoryViewModel by lazy {
        ViewModelProvider(this).get(WeatherHistoryViewModel::class.java)
    }

    companion object {
        fun newInstance() = ResultWeatherHistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultWeatherHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getAllHistory()
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
            is UpdateState.SuccessWeatherHistory -> {
                binding.weatherHistoryFragmentRecyclerView.adapter = adapter
                binding.loadingLayout.visibility = View.GONE
//                val dataWeather = updateState.weatherData
                val listUniqueCities = updateState.listUniqueCities
//                adapter.setWeather(dataWeather)
                adapter.setUniqueListCities(listUniqueCities)
                Snackbar.make(binding.root, resources.getString(R.string.success_load_from_database), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
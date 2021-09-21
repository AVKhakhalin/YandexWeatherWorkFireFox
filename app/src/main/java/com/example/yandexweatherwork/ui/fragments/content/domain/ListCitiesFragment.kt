package com.example.yandexweatherwork.ui.fragments.content.domain

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.observers.domain.ListCitiesPublisherDomain
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModel
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModelSetter
import com.example.yandexweatherwork.controller.observers.viewmodels.UpdateState
import com.example.yandexweatherwork.databinding.FragmentListCitiesBinding
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.ConstantsUi
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.example.yandexweatherwork.ui.fragments.content.result.ResultCurrentFragment
import com.google.android.material.snackbar.Snackbar

class ListCitiesFragment(
    private var isDataSetRusInitial: Boolean,
    private var mainChooserSetter: MainChooserSetter,
    private var mainChooserGetter: MainChooserGetter
): Fragment(), OnItemViewClickListener {
    private var _binding: FragmentListCitiesBinding? = null
    private val binding: FragmentListCitiesBinding
        get() {
            return _binding!!
        }
    private var listCitiesFragmentAdapter = ListCitiesFragmentAdapter(this)
    private var weather: MutableList<City>? = null

    // Ссылка на ResultCurrentViewModel
    private lateinit var listCitiesViewModel: ListCitiesViewModel

    companion object {
        fun newInstance(isDataSetRusInitial: Boolean, mainChooserSetter: MainChooserSetter,
                        mainChooserGetter: MainChooserGetter) =
            ListCitiesFragment(isDataSetRusInitial, mainChooserSetter, mainChooserGetter)
    }

    // Создание наблюдателя в domain
    var listCitiesPublisherDomain : ListCitiesPublisherDomain = ListCitiesPublisherDomain()
    //endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Создание viewModel
        listCitiesViewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        // Задание наблюдателя для данного фрагмента (viewModel)
        (context as ListCitiesViewModelSetter).setListCitiesViewModel(listCitiesViewModel)
        // Получение наблюдателя для domain
        listCitiesPublisherDomain = (context as MainActivity).getListSitiesPublisherDomain()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Начальная установка вида кнопки переключения фильтра стран
        if (isDataSetRusInitial) {
            binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_earth)
        } else {
            binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_russia)
        }

        binding.fragmentListCitiesRecyclerView.adapter = listCitiesFragmentAdapter
        listCitiesFragmentAdapter.setOnItemViewClickListener(this)
        binding.fragmentListCitiesFAB.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mainChooserGetter?.let{
                    val invertedFilterCountry: String = (if (it.getDefaultFilterCountry()
                        == ConstantsUi.FILTER_RUSSIA) ConstantsUi.FILTER_NOT_RUSSIA
                    else ConstantsUi.FILTER_RUSSIA)
                    if (it.getKnownCites("", invertedFilterCountry)!!.size > 0) {
                        isDataSetRusInitial = !isDataSetRusInitial
                        if(!isDataSetRusInitial){
                            binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_russia)
                            with(listCitiesPublisherDomain) {
                                notifyDefaultFilterCountry(ConstantsUi.FILTER_NOT_RUSSIA)
                                notifyDefaultFilterCity(ConstantsUi.DEFAULT_FILTER_CITY)
                            }
                            listCitiesViewModel.getListCities()
                        }else {
                            binding.fragmentListCitiesFAB.setImageResource(R.drawable.ic_earth)
                            with(listCitiesPublisherDomain) {
                                notifyDefaultFilterCountry(ConstantsUi.FILTER_RUSSIA)
                                notifyDefaultFilterCity(ConstantsUi.DEFAULT_FILTER_CITY)
                            }
                            listCitiesViewModel.getListCities()
                        }
                    } else {
                        Snackbar.make(view, "${resources.getString(R.string.error)}: ${resources.getString(R.string.error_no_such_places)}", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
        listCitiesViewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        listCitiesViewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<UpdateState> { updateState: UpdateState ->
                renderData(updateState)
            })
        listCitiesViewModel.getListCities()
    }

    private fun renderData(updateState: UpdateState) {
        when (updateState) {
            is UpdateState.ListCities -> {
                weather = updateState.mainChooserGetter.getKnownCites()
                weather?.let {
                    // Передача в адаптер списка городов
                    listCitiesFragmentAdapter.setWeather(weather!!)
                }
            }
            //else -> //TODO: Добавить случай с неуспешной загрузкой списка
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(city: City) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_result_weather_container, ResultCurrentFragment.newInstance(city,
                mainChooserSetter, mainChooserGetter))
            .commit()
    }

    //region МЕТОДЫ ДЛЯ РАБОТЫ С КОНТЕКСТНЫМ МЕНЮ У ЭЛЕМЕНТОВ СПИСКА
    // Создание контекстного меню для элемента списка
    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.context_menu_action_delete_city -> {
                // Удаление места (города) из списка
//                Toast.makeText(context, "Удаление места ${listCitiesFragmentAdapter
//                    .getPositionChoosedElement()}", Toast.LENGTH_SHORT).show()
                val positionChoosedElement: Int = listCitiesFragmentAdapter
                    .getPositionChoosedElement()
                mainChooserSetter?.let{
                    if (weather != null) {
                        it.removeCity(weather?.get(positionChoosedElement)!!.name,
                            weather?.get(positionChoosedElement)!!.country)
                    }
                }
                listCitiesFragmentAdapter.notifyItemChanged(positionChoosedElement)
                weather?.let{
                    it.removeAt(positionChoosedElement)
                    // Передача в адаптер обновлённого списка городов
                    listCitiesFragmentAdapter.setWeather(weather!!, positionChoosedElement)
                }
                // Установка признака редактирования пользователем списка мест
                mainChooserSetter.setUserCorrectedCityList(true)
            }
            R.id.context_menu_action_show_card -> {
                Toast.makeText(context, "Показать карточку места ${listCitiesFragmentAdapter
                    .getPositionChoosedElement()}", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onContextItemSelected(item)
    }
    //endregion
}
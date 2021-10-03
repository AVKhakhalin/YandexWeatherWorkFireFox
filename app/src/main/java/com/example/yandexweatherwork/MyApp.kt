package com.example.yandexweatherwork

import com.example.yandexweatherwork.repository.facadesettings.room.HistoryDAO
import com.example.yandexweatherwork.repository.facadesettings.room.HistoryDataBase

import android.app.Application
import androidx.room.Room
import java.lang.IllegalStateException

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object{
        private var appInstance:MyApp? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "WeatherHistoryDataBase.db"

        fun getHistoryDAO(): HistoryDAO {
            if(db == null){
                if(appInstance!=null){
                    db = Room.databaseBuilder(appInstance!!.applicationContext,HistoryDataBase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()
                }else{
                    throw  IllegalStateException("appInstance==null")
                }
            }
            return db!!.historyDAO()
        }
    }
}
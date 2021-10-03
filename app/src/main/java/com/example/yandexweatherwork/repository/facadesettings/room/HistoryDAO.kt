package com.example.yandexweatherwork.repository.facadesettings.room

import androidx.room.*

@Dao
interface HistoryDAO {
    // Получение всех записей в базе данных
    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    // Удаление записи по номеру id
    @Query("DELETE FROM HistoryEntity WHERE id=:idForDelete")
    fun deleteQ(idForDelete: Long)

    // Добавление записи в базу данных
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    // Получение всей погодной истории данного места (города)
    // Получение всех полей таблицы из записи, у которых заданное поле name
    @Query("SELECT * FROM HistoryEntity WHERE name LIKE :name")
    fun getDataByWord(name: String): List<HistoryEntity>

    //    @Delete
//    fun delete(entity: HistoryEntity)
    @Query("DELETE FROM HistoryEntity")
    fun deleteAll()

    //    @Update
//    fun update(entity: HistoryEntity)
    @Query("UPDATE HistoryEntity SET name=:newName WHERE id=:idForUpdate")
    fun updateDataById(idForUpdate: Long, newName: String)

    // Получение списка уникальных мест, по которым есть записи погоды
    @Query("SELECT DISTINCT name FROM HistoryEntity")
    fun getUniqueListCities(): List<String>
}
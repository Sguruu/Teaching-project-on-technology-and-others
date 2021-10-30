package com.example.roomstudy.room

import android.app.Person
import androidx.room.*

/***
 * В объекте Dao мы будем описывать методы для работы с базой данных.
 * Нам нужны будут методы для получения списка персон и для
 * добавления/изменения/удаления сотрудников.
 */

@Dao
interface PersonDao {

    // получить полный список сотрудников
    @Query("SELECT * FROM Person")
    fun getAll(): List<EntityPerson>

    // получить сотрудника по id
    @Query("SELECT * FROM person WHERE id = :id")
    fun getById(id: Long) : EntityPerson

    @Insert
    fun insert(person: EntityPerson)

    @Update
    fun update(person: EntityPerson)

    @Delete
    fun delete(person: EntityPerson)

}
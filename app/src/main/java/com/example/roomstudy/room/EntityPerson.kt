package com.example.roomstudy.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * Аннотацией Entity нам необходимо пометить объект, который мы хотим хранить в базе данных.
 * Для этого создаем класс EntityPerson, который будет представлять собой данные человека:
 * id, имя, фамилия, возраст:
 */

@Entity(tableName = "Person")
data class EntityPerson(

    /*
    Аннотацией PrimaryKey мы помечаем поле, которое будет ключом в таблице.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    var name: String?,

    @ColumnInfo(name = "last_name")
    var lastName: String?,

    var age: Int? ,

    )
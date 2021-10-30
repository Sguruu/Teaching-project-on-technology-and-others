package com.example.roomstudy.room

import androidx.room.Database
import androidx.room.RoomDatabase

/***
 * В параметрах аннотации Database указываем, какие Entity будут использоваться, и версию базы.
 * Для каждого Entity класса из списка entities будет создана таблица.
 * В Database классе необходимо описать абстрактные методы для получения Dao объектов,
 * которые вам понадобятся.
 */
@Database(entities = [EntityPerson::class],version = 2)
abstract class  AppDatabase : RoomDatabase() {
    abstract fun personDao():PersonDao
}
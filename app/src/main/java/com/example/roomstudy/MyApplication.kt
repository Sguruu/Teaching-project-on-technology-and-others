package com.example.roomstudy

import android.app.Application
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.example.roomstudy.room.AppDatabase

/***
 * Данный класс необходимо добавить в манифест android:name=".MyApplication"
 *
 * ожно использовать Application класс для создания и хранения AppDatabase
 * т.к. Учитывайте, что при вызове этого кода Room каждый раз будет создавать новый экземпляр
 * AppDatabase. Эти экземпляры очень тяжелые и рекомендуется использовать один экземпляр для всех
 * ваших операций. Поэтому вам необходимо позаботиться о синглтоне для этого объекта.
 */

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    lateinit var database: AppDatabase


    override fun onCreate() {
        super.onCreate()
        instance = this
        database = databaseBuilder(this, AppDatabase::class.java, "database")
            .allowMainThreadQueries() // В этом случае вы не будете получать Exception при работе в UI потоке. Но вы должны понимать, что это плохая практика, и может добавить ощутимых тормозов вашему приложению.
            .build()
    }
}
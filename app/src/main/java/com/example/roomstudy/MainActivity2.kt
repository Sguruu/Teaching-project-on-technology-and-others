package com.example.roomstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomstudy.room.AppDatabase
import com.example.roomstudy.room.PersonDao

class MainActivity2 : AppCompatActivity() {

    private val SEND_KEY_PERSON = "SEND_KEY_PERSON"
    private val SEND_KEY_PERSON_BOOLEAN = "${SEND_KEY_PERSON}_BOOLEAN"
    private val SEND_KEY_PERSON_ID = "${SEND_KEY_PERSON}_ID"

    var recyclerView: RecyclerView? = null
    var listPerson: MutableList<DataMyAdapter>? = null
    var customClickAdapter: CustomClickAdapter? = null
    var database: AppDatabase? = null
    var personDAO: PersonDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Log.i("LifeCycle", "MainActivity2 onCreate")

        val arrow: Button = findViewById(R.id.arrow)

        recyclerView = findViewById(R.id.recyclerView)
        database = MyApplication.instance.database
        personDAO = database?.personDao()

        // мапинг листа с БД для адаптера
        gettingFromBD()

        // создание объекта
        customClickAdapter = object : CustomClickAdapter {
            override fun <T> customClick(value: Int, position: Int, obValue: T) {

                // присваиваем тип
                obValue as DataMyAdapter

                when (value) {
                    0 -> {
                        // нажать на удалить запись
                        if (obValue.id != null) {
                            personDAO?.delete(personDAO!!.getById(obValue.id))
                            listPerson?.remove(obValue)
                            recyclerView?.adapter?.notifyItemRemoved(position)
                        }
                    }
                    1 -> {
                        // нажать на редактировать запись
                        val intent = Intent(this@MainActivity2, MainActivity::class.java)
                        intent.putExtra(SEND_KEY_PERSON_ID, obValue.id)
                        intent.putExtra(SEND_KEY_PERSON_BOOLEAN, true)
                        startActivity(intent)
                    }
                }
            }
        }

        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = MyAdapter(listPerson ?: mutableListOf(), customClickAdapter)

        arrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("LifeCycle", "MainActivity2 onStart")
    }

    override fun onResume() {
        super.onResume()
        // мапинг листа с БД для адаптера
        gettingFromBD()
        recyclerView?.adapter = MyAdapter(listPerson ?: mutableListOf(), customClickAdapter)
        Log.i("LifeCycle", "MainActivity2 onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("LifeCycle", "MainActivity2 onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("LifeCycle", "MainActivity2 onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("LifeCycle", "MainActivity2 onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LifeCycle", "MainActivity2 onDestroy")
        // чистим память
        recyclerView = null
        listPerson = null
        customClickAdapter = null
        database = null
        personDAO = null
    }

    /*
    мапинг листа с БД для адаптера
     */
    private fun gettingFromBD() {
        listPerson = mutableListOf()
        listPerson?.addAll(personDAO?.getAll()!!.map { it ->
            DataMyAdapter(it.id, it.name, it.lastName, it.age)
        })
    }
}

/***
 * CustomClickAdapter
 *
 * Класс для создания коллбека в RecyclerView и вынесения логики в MainActivity2
 */

interface CustomClickAdapter {
    fun <T> customClick(value: Int, position: Int, obValue: T)
}
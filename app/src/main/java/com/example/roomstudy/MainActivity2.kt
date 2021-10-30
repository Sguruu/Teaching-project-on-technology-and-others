package com.example.roomstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity2 : AppCompatActivity() {

    private val SEND_KEY_PERSON = "SEND_KEY_PERSON"
    private val SEND_KEY_PERSON_BOOLEAN = "${SEND_KEY_PERSON}_BOOLEAN"
    private val SEND_KEY_PERSON_ID = "${SEND_KEY_PERSON}_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val arrow: Button = findViewById(R.id.arrow)

        val database = MyApplication.instance.database
        val personDAO = database.personDao()

        // мапинг листа с БД для адаптера
        val listPerson: MutableList<DataMyAdapter> = mutableListOf()
        listPerson.addAll(personDAO.getAll().map { it ->
            DataMyAdapter(it.id, it.name, it.lastName, it.age)
        })

        // создание объекта
        val customClickAdapter: CustomClickAdapter
        customClickAdapter = object : CustomClickAdapter {
            override fun <T> customClick(value: Int, position: Int, obValue: T) {

                // присваиваем тип
                obValue as DataMyAdapter

                when (value) {
                    0 -> {
                        // нажать на удалить запись
                        if (obValue.id != null) {
                            personDAO.delete(personDAO.getById(obValue.id))
                            listPerson.remove(obValue)
                            recyclerView.adapter?.notifyItemRemoved(position)
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

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = MyAdapter(listPerson, customClickAdapter)

        arrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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
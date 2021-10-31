package com.example.roomstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.roomstudy.room.EntityPerson

class MainActivity : AppCompatActivity() {

    /*
    Ключи получения данных с MainActivity2
     */
    private val RECEIPT_KEY_PERSON = "SEND_KEY_PERSON"
    private val RECEIPT_KEY_PERSON_BOOLEAN = "${RECEIPT_KEY_PERSON}_BOOLEAN"
    private val RECEIPT_KEY_PERSON_ID = "${RECEIPT_KEY_PERSON}_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("LifeCycle", "MainActivity onCreate")

        //принимает данные с MainActivity2
        val idPerson: Long = intent.getLongExtra(RECEIPT_KEY_PERSON_ID, 0)
        val idBoolean: Boolean = intent.getBooleanExtra(RECEIPT_KEY_PERSON_BOOLEAN, false)

        val textView = findViewById<TextView>(R.id.textView)
        val name = findViewById<EditText>(R.id.name)
        val lastName = findViewById<EditText>(R.id.lastName)
        val age = findViewById<EditText>(R.id.age)
        val save = findViewById<Button>(R.id.save)
        val viewData = findViewById<Button>(R.id.viewData)

        val _intent = Intent(this, MainActivity2::class.java)
        /*
           Установка флага позволяет не пересоздавать активти, а перезапускать ее, что гаранитурет
           нам наличие одного экземпляра активти. Вместо медота onCreate будет вызваться метод
           onRestart у активности.
           FLAG_ACTIVITY_NEW_TASK - новая задача
           FLAG_ACTIVITY_SINGLE_TOP - один верх
           FLAG_ACTIVITY_CLEAR_TOP - удалить верх
           FLAG_ACTIVITY_REORDER_TO_FRONT - переправка действия флага на передний

           Следующее сочитание флагов создает по одному экземпляру каждой активти.
           FLAG_ACTIVITY_NEW_TASK
           FLAG_ACTIVITY_REORDER_TO_FRONT

           FLAG_ACTIVITY_SINGLE_TOP оставляет только одну активти вверху (вызывает у второй активти
           onDestroy)
           FLAG_ACTIVITY_CLEAR_TOP удаляет верхушку и заменяет ее (вызывает у второй активти
           onDestroy)
            */
        _intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
               // Intent.FLAG_ACTIVITY_SINGLE_TOP
              //  Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

        //БД
        val database = MyApplication.instance.database
        val personDAO = database.personDao()

        //отрисовка данных с другой активти
        if (idBoolean) {
            val entityPerson = personDAO.getById(idPerson)
            name.setText(entityPerson.name.toString())
            name.setText(entityPerson.name ?: " ")
            lastName.setText(entityPerson.lastName ?: " ")
            age.setText(entityPerson.age.toString() ?: " ")
        }

        /*
        Прослушиватели кнопок
         */
        viewData.setOnClickListener {
            startActivity(_intent)
        }

        save.setOnClickListener {
            val ageT = when (age.text.toString()) {
                "" -> null
                "null" -> null
                else -> Integer.parseInt(age.text.toString())
            }
            when (idBoolean) {
                false -> {
                    // добавления сотрудника в БД
                    val entityPerson: EntityPerson = EntityPerson(
                        null,
                        name = name.text.toString(),
                        lastName = lastName.text.toString(),
                        ageT
                    )
                    // добавление в БД
                    personDAO.insert(entityPerson)
                }
                true -> {
                    // обновление базы
                    val entityPerson: EntityPerson = EntityPerson(
                        idPerson,
                        name = name.text.toString(),
                        lastName = lastName.text.toString(),
                        ageT
                    )
                    personDAO.update(entityPerson)
                }
            }
            // обнуление полей
            name.text = null
            lastName.text = null
            age.text = null
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("LifeCycle", "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("LifeCycle", "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("LifeCycle", "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("LifeCycle", "MainActivity onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("LifeCycle", "MainActivity onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LifeCycle", "MainActivity onDestroy")
    }
}
package com.example.roomstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}
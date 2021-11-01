package com.example.roomstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.roomstudy.room.AppDatabase
import com.example.roomstudy.room.EntityPerson
import com.example.roomstudy.room.PersonDao

class MainActivity : AppCompatActivity() {

    /*
    Ключи получения данных с MainActivity2
     */
    private val RECEIPT_KEY_PERSON = "SEND_KEY_PERSON"
    private val RECEIPT_KEY_PERSON_BOOLEAN = "${RECEIPT_KEY_PERSON}_BOOLEAN"
    private val RECEIPT_KEY_PERSON_ID = "${RECEIPT_KEY_PERSON}_ID"

    private var idPerson: Long = 0
    private var idBoolean: Boolean = false

    private var database: AppDatabase? = null
    private var personDAO: PersonDao? = null

    private var age: EditText? = null
    private var name: EditText? = null
    private var lastName: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("LifeCycle", "MainActivity onCreate")

        //принимает данные с MainActivity2
        idPerson = intent.getLongExtra(RECEIPT_KEY_PERSON_ID, 0)
        idBoolean = intent.getBooleanExtra(RECEIPT_KEY_PERSON_BOOLEAN, false)

        val textView = findViewById<TextView>(R.id.textView)
        val save = findViewById<Button>(R.id.save)
        val viewData = findViewById<Button>(R.id.viewData)

        val _intent = Intent(this, MainActivity2::class.java)

        //БД
        database = MyApplication.instance.database
        personDAO = database?.personDao()

        age = findViewById(R.id.age)
        name = findViewById<EditText>(R.id.name)
        lastName = findViewById<EditText>(R.id.lastName)

        //отрисовка данных с другой активти
        fillingInTheFields()

        /*
        Прослушиватели кнопок
         */
        viewData.setOnClickListener {
            startActivity(_intent)
        }

        save.setOnClickListener {
            val ageT = when (age?.text.toString()) {
                "" -> null
                "null" -> null
                else -> Integer.parseInt(age?.text.toString())
            }
            when (idBoolean) {
                false -> {
                    // добавления сотрудника в БД
                    val entityPerson: EntityPerson = EntityPerson(
                        null,
                        name = name?.text.toString(),
                        lastName = lastName?.text.toString(),
                        ageT
                    )
                    // добавление в БД
                    personDAO?.insert(entityPerson)
                }
                true -> {
                    // обновление базы
                    val entityPerson: EntityPerson = EntityPerson(
                        idPerson,
                        name = name?.text.toString(),
                        lastName = lastName?.text.toString(),
                        ageT
                    )
                    personDAO?.update(entityPerson)
                }
            }
            // обнуление полей
            name?.text = null
            lastName?.text = null
            age?.text = null
        }

    }

    override fun onStart() {
        super.onStart()
        Log.i("LifeCycle", "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("LifeCycle", "MainActivity onResume")
        fillingInTheFields()
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
        Log.i("LifeCycle", "MainActivity onRestart ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LifeCycle", "MainActivity onDestroy")
    }

    // принимает данные с MainActivity2
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            idPerson = intent.getLongExtra(RECEIPT_KEY_PERSON_ID, 0)
            idBoolean = intent.getBooleanExtra(RECEIPT_KEY_PERSON_BOOLEAN, false)
        }

    }

    /*
    Custom fun
     */
    // заполнение полей
    private fun fillingInTheFields() {
        if (idBoolean) {
            val entityPerson = personDAO?.getById(idPerson)
            name?.setText(entityPerson?.name ?: " ")
            lastName?.setText(entityPerson?.lastName ?: " ")
            when (entityPerson?.age) {
                null -> {
                    age?.setText("")
                }
                else -> {
                    age?.setText(entityPerson.age.toString())
                }
            }
        }
    }
}
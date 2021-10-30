# Изучение БД Room 
## Version 0.1
------------------

Реализация простого приложения с БД:
1. Приложение добавляет данные в БД
2. Приложение читает данные с БД
3. Приложение редактирует данные с БД 

[Быстрый переход к коду проекта](https://github.com/Sguruu/Teaching-project-on-technology-and-others/tree/CleanRoom/app/src/main/java/com/example/roomstudy)

## Использованные источники:
[Старт Андройд](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/530-urok-6-room-entity.html)
##

# Важные моменты 
```no-highlight
БД инициализируется в классе MyApplication

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    lateinit var database: AppDatabase


    override fun onCreate() {
        super.onCreate()
        instance = this
        database = databaseBuilder(this, AppDatabase::class.java, "database")
            .allowMainThreadQueries() /*
            В этом случае вы не будете получать Exception при работе в UI потоке. 
            Но вы должны понимать, что это плохая практика, и может добавить ощутимых тормозов вашему приложению.
            */
            .build()
    }
}

**********************************
Проект работает в UI потоке, что является грубым нарушение, следующий этап работа в Room с помощью RxJava или Corutin 
Добавление зависимости в build.gradle проекта 
plugins {
    id 'kotlin-kapt'
}
dependencies {
 // подключаем Room
    def room_version = "2.3.0"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}


```
# Стадия готовности проекта : ГОТОВ


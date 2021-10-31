# Изучение БД Room (Version 1.0)


Реализация простого приложения с БД:
1. Приложение добавляет данные в БД
2. Приложение читает данные с БД
3. Приложение редактирует данные с БД 
4. Удаление с БД

[Быстрый переход к коду проекта](https://github.com/Sguruu/Teaching-project-on-technology-and-others/tree/CleanRoom/app/src/main/java/com/example/roomstudy)

## Использованные источники:
[Старт Андройд](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/530-urok-6-room-entity.html)
##

# Важные моменты 
```kotlin
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
Проект работает в UI потоке, что является грубым нарушением, следующий этап работа в Room с помощью RxJava или Corutin 
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
# [Стоит обрать внимание на переход между активностями и использования флагов](https://github.com/Sguruu/Teaching-project-on-technology-and-others/blob/CleanRoom/app/src/main/java/com/example/roomstudy/MainActivity.kt)
```kotlin
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
	startActivity(_intent)

```
# Стадия готовности проекта : ГОТОВ


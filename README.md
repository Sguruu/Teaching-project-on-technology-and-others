# Изучение БД Room (Version 1.0)


Реализация простого приложения с БД:
1. Приложение добавляет данные в БД
2. Приложение читает данные с БД
3. Приложение редактирует данные с БД 
4. Удаление с БД

[Быстрый переход к коду проекта](https://github.com/Sguruu/Teaching-project-on-technology-and-others/tree/CleanRoom/app/src/main/java/com/example/roomstudy)

## Использованные источники:
Room:
1. [Старт Андройд](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/530-urok-6-room-entity.html)
2. [Документация Андройд](https://developer.android.com/training/data-storage/room)
3. [Документация Launch Mode]()
4. [Статья разбираемся с Launch Mode]()
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
# Особенности работы с активностями при переходе 

Активти созадются 1 раз и просто пересоздаются. При пересоздании активности, а не создания, есть особенности передачи данных между активностями.
```kotlin
// принимает данные с MainActivity2
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            idPerson = intent.getLongExtra(RECEIPT_KEY_PERSON_ID, 0)
            idBoolean = intent.getBooleanExtra(RECEIPT_KEY_PERSON_BOOLEAN, false)
        }

    }
```
manifest

добавление android:launchMode для двух активностей (singleTask/singleInstance)

[launchMode:](https://developer.android.com/guide/topics/manifest/activity-element)
1. standard (создает новую активность)
Default. The system always creates a new instance of the activity in the target task and routes the intent to it.
2. singleTop (условно создает новую активность)
If an instance of the activity already exists at the top of the target task, the system routes the intent to that instance through a call to its onNewIntent() method, rather than creating a new instance of the activity.
3. singleTask (не создает новой активности)
The system creates the activity at the root of a new task and routes the intent to it. However, if an instance of the activity already exists, the system routes the intent to existing instance through a call to its onNewIntent() method, rather than creating a new one.
4. singleInstance (не создает новой активности)
Same as "singleTask", except that the system doesn't launch any other activities into the task holding the instance. The activity is always the single and only member of its task.
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.roomstudy">

    <application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.RoomStudy">
        <activity
            android:launchMode="singleTask"
            android:name=".MainActivity2"
            android:exported="true" />
        <activity
            android:launchMode="singleInstance"
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

# Стоит обрать внимание на переход между активностями и использования флагов

Работает только с API 30 (Android 11)
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


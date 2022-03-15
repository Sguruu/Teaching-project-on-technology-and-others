# WorkManager (Version 0.2)


Описание
1. Приложение реализует фоновую работу WorkManager, запускается сразу при запуске приложения, результат выводится в логах.  

[Быстрый переход к коду проекта](https://github.com/Sguruu/Teaching-project-on-technology-and-others/tree/WorkManager/app/src/main/java/com/example/workmanager)

## Использованные источники:
[WorkManager. Введение](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/562-urok-29-workmanager-vvedenie.html)

[WorkManager. Критерии запуска задачи](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/563-urok-30-workmanager-constraints.html)

[WorkManager. Последовательность выполнения задач](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/564-urok-31-workmanager-posledovatelnosti-vypolnenija-zadach.html)

[WorkManager. Передача и получение данных](https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/565-urok-32-workmanager-peredacha-i-poluchenie-dannyh.html)

[Фоновая работа в Android: обзор возможностей WorkManager](https://habr.com/ru/company/simbirsoft/blog/553912/)

[Getting started with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/basics#groovy)

[Advanced WorkManager topics](https://developer.android.com/topic/libraries/architecture/workmanager/advanced)


##

# Важные моменты 
Подключение к проекту 
```.gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    compileSdk 31

    defaultConfig {
        applicationId "com.example.workmanager"
        minSdk 26
        targetSdk 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.4.0'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.2'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    def work_version='2.7.1'

    /* WorkManager */
    // (Java only)
    implementation "androidx.work:work-runtime:$work_version"

    // Kotlin + coroutines
    implementation "androidx.work:work-runtime-ktx:$work_version"

    // optional - RxJava2 support
    implementation "androidx.work:work-rxjava2:$work_version"

    // optional - GCMNetworkManager support
    implementation "androidx.work:work-gcm:$work_version"

    // optional - Test helpers
    androidTestImplementation "androidx.work:work-testing:$work_version"

    // optional - Multiprocess support
    implementation "androidx.work:work-multiprocess:$work_version"
}
```
Класс MyWork, реализация интерфейса Worker
```.java
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "workmng"

    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        // Тут пишется код для выполнения сервиса
        try {
            // ждем 10 секунд
            TimeUnit.SECONDS.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Log.d(TAG, "doWork: end")
        Result.retry()
        Result.failure()
        Result.retry()
        /*
      Вернуть можно три параметра :
      Result.failure() - Задача завершилась ошибкой
      Result.retry() - Задача завершилась с ошибкой, будет перезапуск
      Result.success - Задача завершилась успехом
        */
        return Result.retry()
    }

    override fun onStopped() {
        // Вызывается при отмене задачи из кода
        super.onStopped()
        Log.d(TAG, "onStopped");
    }
}
```
# WorkManager Строитель и параметры объявления 
Объявление задачи которая будет выполняться один раз
```.java  val myWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest
            .Builder(MyWorker::class.java)
            // добавляем тег нашей задачи
            // У WorkInfo есть метод getTags,
            // который вернет все теги, которые присвоены этой задаче.
            .addTag("myTag")
            // задача запускается спустя 10 секунд после передачи ее в .enqueue
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build() 
```
Периодическая задача 
```.java
 val myWorkRequestPeriod: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 30, TimeUnit.MINUTES)
                /*
                 Если нужно многократное выполнение через определенный период времени,
                 то можно использовать PeriodicWorkRequest.

                 В билдере задаем интервал в 30 минут. Теперь задача будет выполняться с этим интервалом.
                 Минимально доступный интервал - 15 минут. Если поставите меньше,
                 WorkManager сам повысит до 15 минут.

                 WorkManager гарантирует, что задача будет запущена один раз в течение
                 указанного интервала. И это может случиться в любой момент
                 интервала - через 1 минуту, через 10 или через 29.

                 PeriodicWorkRequest.Builder(MyWorker.class, 30, TimeUnit.MINUTES, 25, TimeUnit.MINUTES)
                 С помощью параметра flex можно ограничить разрешенный диапазон времени запуска.
                 Кроме интервала в 30 минут дополнительно передаем в билдер flex параметр 25
                 минут. Теперь задача будет запущена не в любой момент 30-минутного интервала,
                 а только после 25-й минуты. Т.е. между 25 и 30 минутами.
                 */
                .build()
    }
```
Запуск задачи 
```.java WorkManager.getInstance(this)
            .enqueue(myWorkRequest) 
```
Отслеживание статуса задачи 
```.java
 WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(myWorkRequest.id).observe(
                this,
                object : Observer<WorkInfo> {
                    override fun onChanged(t: WorkInfo?) {
                        // Статус приходить в UI потоке
                        Log.d(TAG, "onChanged: ${t?.state}")
                    }
                }
            )
```
Отмена задачи 
```.java
 // Отмена задачи по id, будет вызываться метод onStopped
        // cancelAllWork - отменяет все задачи, не рекомендован к использованию
        // задачу также можно отменить все задачи
        // по тегу WorkManager.getInstance(this).cancelAllWorkByTag("")
        WorkManager.getInstance(this).cancelWorkById(myWorkRequest.id)
```
При построении ворк менеджера можно указывать критерии запуска задач 
```.java
val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

val myWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest
            .Builder(MyWorker1::class.java)
            // параметр ограничения
            .setConstraints(constraints)
            .build()
```
Критерии которые можно использовать 
```.java
Возмонжные параметры :
        setRequiresCharging (boolean requiresCharging) - зарядное устройство должно быть включено
        setRequiresBatteryNotLow (boolean requiresBatteryNotLow) - уровень батареи не ниже критического
        setRequiredNetworkType (NetworkType networkType) - наличие интернета
        setRequiresDeviceIdle (boolean requiresDeviceIdle) - Критерий: девайс не используется
        какое-то время и ушел в спячку. Работает на API 23 и выше.
        setRequiresStorageNotLow (boolean requiresStorageNotLow) - на девайсе должно быть свободное
        место, не меньше критического порога
        addContentUriTrigger (Uri uri, boolean triggerForDescendants) - задача запустится,
        когда обновится содержимое указанного Uri  Работает на API 24 и выше.
```
# Способы запуска задач
Одновременный запуск задач 
```.java
WorkManager.getInstance(this)
.enqueue(mutableListOf(myWorkRequest,myWorkRequest1,myWorkRequest2))
```
Последовательный запуск задач
```java
 WorkManager.getInstance(this)
            // передаем первую задачу и создаем тем самом последовательность задач
            .beginWith(myWorkRequest)
            // передаем следующую задачу
            .then(myWorkRequest1)
            .then(myWorkRequest2)
            .enqueue()
```
Комбинированный запуск задач
```.java
WorkManager.getInstance(this)
            .beginWith(mutableListOf(myWorkRequest, myWorkRequest1))
            .then(myWorkRequest1)
            .then(mutableListOf(myWorkRequest2, myWorkRequest1))
            .enqueue()
            
   /*
        Условие - нужно чтобы myWorkRequest1 выполнился после myWorkRequest
        myWorkRequest3 и myWorkRequest4 выполнился после myWorkRequest2,
        а myWorkRequest5 выполнился после выполнения прошлых последовательностей
        */
        val chain01 = WorkManager.getInstance(this)
            .beginWith(myWorkRequest)
            .then(myWorkRequest1)

        val chain34 = WorkManager.getInstance(this)
            .beginWith(myWorkRequest2)
            .then(mutableListOf(myWorkRequest3, myWorkRequest4))

        WorkContinuation.combine(mutableListOf(chain01, chain34))
            .then(myWorkRequest5)
            .enqueue()           
```
Уникальная последовательность задач
```.java
 WorkManager.getInstance(this)
            .beginUniqueWork("work123", ExistingWorkPolicy.REPLACE, myWorkRequest)
            .then(myWorkRequest1)
            .enqueue()
    }
    
     /*
        Unique work
        Мы можем сделать последовательность задач уникальной.
        Для этого начинаем последовательность методом beginUniqueWork.
        В качестве режима мы указали REPLACE. Это означает, что,
        если последовательность с таким именем уже находится в работе, то еще один запуск приведет
        к тому, что текущая выполняемая последовательность будет остановлена, а новая запущена.
        Режим ExistingWorkPolicy.KEEP оставит в работе текущую выполняемую последовательность. А новая будет проигнорирована.
        Режим ExistingWorkPolicy.APPEND запустит новую последовательность после выполнения текущей.
         */    
```

# Стадия готовности проекта : В ПРОЦЕССЕ 

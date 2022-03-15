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
       

# Стадия готовности проекта : В ПРОЦЕССЕ 

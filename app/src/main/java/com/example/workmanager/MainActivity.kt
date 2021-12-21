package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val TAG = "workmng"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        WorkRequest позволяет нам задать условия запуска и входные параметры к задаче.
        Пока что мы ничего не задаем, а просто создаем OneTimeWorkRequest, которому говорим,
        что запускать надо будет задачу MyWorker.
        OneTimeWorkRequest не зря имеет такое название.
        Эта задача будет выполнена один раз. Есть еще PeriodicWorkRequest.
        */
        val myWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest
            .Builder(MyWorker::class.java)
            // добавляем тег нашей задачи
            // У WorkInfo есть метод getTags,
            // который вернет все теги, которые присвоены этой задаче.
            .addTag("myTag")
            // задача запускается спустя 10 секунд после передачи ее в .enqueue
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueue(myWorkRequest)


        // Отслеживаем статус задачи
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

        // Отмена задачи по id, будет вызываться метод onStopped
        // cancelAllWork - отменяет все задачи, не рекомендован к использованию
        // задачу также можно отменить все задачи
        // по тегу WorkManager.getInstance(this).cancelAllWorkByTag("")
        WorkManager.getInstance(this).cancelWorkById(myWorkRequest.id)

        // Переодическая задача
        val myWorkRequestPeriod:PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 30 , TimeUnit.MINUTES)
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
}
package com.example.workmanager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val TAG = "workmng"
    private val TAG1 = "workmng1"
    private val TAG2 = "workmng2"
    private val TAG3 = "workmng3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //WorkManagerV0()
        //WorkManagerV1()
        // WorkManagerV2()
        WorkManagerV3()


    }

    // WorkManager Введение 0
    private fun WorkManagerV0() {
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

    // WorkManager. Критерии запуска задачи.
    private fun WorkManagerV1() {

        // код добавления критерия для запуска задачи
        /*
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
         */
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val myWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest
            .Builder(MyWorker1::class.java)
            // параметр ограничения
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueue(myWorkRequest)

        // Отслеживаем статус задачи
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(myWorkRequest.id)
            .observe(this,
                {
                    Log.d(TAG1, "onChanged: ${it.state}")
                })
    }

    // Последовательность выполнения задач
    @SuppressLint("EnqueueWork")
    private fun WorkManagerV2() {

        val myWorkRequest = OneTimeWorkRequest
            .Builder(MyWorker2::class.java)
            .build()

        val myWorkRequest1 = OneTimeWorkRequest
            .Builder(MyWorker2_1::class.java)
            .build()

        val myWorkRequest2 = OneTimeWorkRequest
            .Builder(MyWorker2_2::class.java)
            .build()

        val myWorkRequest3 = OneTimeWorkRequest
            .Builder(MyWorker2_3::class.java)
            .build()

        val myWorkRequest4 = OneTimeWorkRequest
            .Builder(MyWorker2_4::class.java)
            .build()

        val myWorkRequest5 = OneTimeWorkRequest
            .Builder(MyWorker2_5::class.java)
            .build()

        // Данные задачи запустятся одновременно
//        WorkManager.getInstance(this)
//            .enqueue(mutableListOf(myWorkRequest,myWorkRequest1,myWorkRequest2))

        // Последовательный запуск задач
        WorkManager.getInstance(this)
            // передаем первую задачу и создаем тем самом последовательность задач
            .beginWith(myWorkRequest)
            // передаем следующую задачу
            .then(myWorkRequest1)
            .then(myWorkRequest2)
            .enqueue()

        // Комбинирование
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
        WorkManager.getInstance(this)
            .beginUniqueWork("work123", ExistingWorkPolicy.REPLACE, myWorkRequest)
            .then(myWorkRequest1)
            .enqueue()
    }

    // Передача и получение данных
    private fun WorkManagerV3() {
        /*
        Входные данные
        Данные помещаем в объект Data с помощью его билдера.
        Далее этот объект передаем в метод setInputData билдера WorkRequest.
        */
        val myData = Data.Builder()
            .putString("keyA", "value1")
            .putInt("keyB", 1)
            .build()

        val myWorkRequest1 = OneTimeWorkRequest.Builder(MyWorker3::class.java)
            .setInputData(myData)
            .build()

//        WorkManager
//            .getInstance(this)
//            .enqueue(myWorkRequest1)


        // Получение выходных данных
        // https://developer.android.com/topic/libraries/architecture/workmanager/advanced
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(myWorkRequest1.id)
            .observe(this, { info ->
                if (info != null && info.state.isFinished){
                    val myResultString = info.outputData.getString("keyC")
                    val myResultInt = info.outputData.getInt("keyD",0)
                    Log.d(TAG3,"Выходные данные myResultString = $myResultString " +
                            "myResultInt = $myResultInt")
                }
            })

        val myWorkRequest2 = OneTimeWorkRequest.Builder(MyWorker3_1::class.java)
            .build()

        // тут выходные данные из первой задачи должны передаться во вторую
        WorkManager.getInstance(this)
            .beginWith(myWorkRequest1)
            .then(myWorkRequest2)
            .enqueue()

        // также можно использовать InputMerger для преобразования несколько выходных результатов
        // в один входной результат, также имеется возможность создания CustomWorkManager

    }
}

# Проект для тестирования фич (Version 1.0)

Проект содержит класс для тестирования фич, сюда можно подгружать протестинные фичи.

[Класс FeatureTestRun](https://github.com/Sguruu/Teaching-project-on-technology-and-others/blob/TestFeatureMain/src/main/kotlin/testfeature/FeatureTestRun.kt)

[Класс ContractTest](https://github.com/Sguruu/Teaching-project-on-technology-and-others/blob/TestFeatureMain/src/main/kotlin/testfeature/ContractTest.kt)

## Использованные источники:
[Источник](url)
##

# Важные моменты 
Реализация классов для использования теста 
```kotlin

package testfeature

/**
 * Интерфейс контракта для использования класса FeatureTestRun
 *
 * @see testfeature.example.TestExample
 * @see FeatureTestRun
 *
 * @author Mekhonoshin Sergey (telegram : https://t.me/Sgur_3 )
 * @since 1.0
 */
interface ContractTest<T> {
    /**
     * Переменная которая должна содержать в себе имя тестироваемого метода
     */
    var nameMethod: String

    /**
     * Функция, где необходимо присвоить свойству nameMethod имя тестирумеого метода
     */
    fun initTest()
}

```

```kotlin
package testfeature

import java.lang.reflect.Method
import java.util.Date

/**
 *
 * Реализация запуска теста
 * @param featureClass тестируемый класс
 *
 * @see testfeature.example.TestExample
 * @see ContractTest
 *
 * @author Mekhonoshin Sergey (telegram : https://t.me/Sgur_3 )
 * @since 1.1
 */
class FeatureTestRun<T>(private val featureClass: ContractTest<T>) {

    /**
     *  classJava используемый класс для провереки
     */
    private val classJava: Class<*> = featureClass::class.java

    /**
     * список времени работы тестируемого метода
     */
    private val timeWorkList: MutableList<Long> = mutableListOf()

    /**
     * максимальное время выполнения
     */
    private var maxTimeWork: Long? = 0

    /**
     * минимальное время выполнения
     */
    private var minTimeWork: Long? = 0

    /**
     * среднее время выполнения
     */
    private var aweraseTimeWork: Long = 0

    /**
     * Функция запуска тестирования, запускает тестирование метода класса
     * @param testFunctionParameters коллбек с парамметрами тестируемой функции
     * @param numberRun количество запусков для тестирования, значения по умолчанию 100
     */
    fun runTest(
        testFunctionParameters: (() -> Array<*>)?,
        numberRun: Int = 100
    ) {
        // создание списка класса параметров
        val methodParams = mutableListOf<Class<*>>()
        // вызов функции инициализации по контракту
        featureClass.initTest()

        methodParams.addAll(getMethodParams())
        val method: Method = getMethodCustom(methodParams)


        multipleRunMethod(numberRun) {
            val startTime = Date().time
            runMethod(testFunctionParameters, method)
            val endTime = Date().time
            saveStatistics(endTime - startTime)
        }
        statisticsOutput(numberRun, aweraseTimeWork(numberRun))
    }

    /**
     * Получение класса параметров функции по ее имени
     * @return Возвращает список содержащий класс ожидаемых параметров для функции MutableList<Class<*>>
     */
    private fun getMethodParams(): MutableList<Class<*>> {
        // создание списка параметров
        val methodParams = mutableListOf<Class<*>>()
        // цикл по списку всех методов в классе
        classJava.methods.forEach { currentMethod ->
            // условие для находения нашего метода по имени
            if (featureClass.nameMethod == currentMethod.name) {
                // цикл по параметрам найденного метода
                currentMethod.parameters.forEach { parameter ->
                    // получение имени типа параметра
                    val nameMethodParams: String = parameter.type.simpleName
                    println("nameMethodParams $nameMethodParams")
                    // маппинг по имени типов параметров
                    nameMethodParams.toClass()?.let { methodParams.add(it) }
                }
            }
        }
        return methodParams
    }

    /**
     * Получить метод из класса
     * @param methodParams ожидаемый тип параметров для искомой функции
     * @return  Method  - искомая функция класса
     */
    private fun getMethodCustom(methodParams: MutableList<Class<*>>): Method {
        return methodParams.isNotEmpty().let {
            if (it) {
                println("*methodParams.toTypedArray() ${methodParams.toTypedArray()}")
                classJava.getMethod(featureClass.nameMethod, *methodParams.toTypedArray())
            } else {
                classJava.getMethod(featureClass.nameMethod)
            }
        }
    }

    /**
     * Запуск n раз передаваемого метода
     * @param numberRun количество необходимых итераций запуска/прогона метода
     * @param callbackMethod коллбек содержащий запускаемый код
     */
    private inline fun multipleRunMethod(numberRun: Int, callbackMethod: () -> Unit) {
        for (i in 1..numberRun) {
            callbackMethod.invoke()
        }
    }

    /**
     * Запуск тестируемого метода
     * @param testFunctionParameters значения параметров тестируемого метода
     * @param method тестируемый метод
     */
    private fun runMethod(testFunctionParameters: (() -> Array<*>)?, method: Method) {
        testFunctionParameters?.let {
            method.invoke(featureClass, *testFunctionParameters.invoke())
        } ?: method.invoke(featureClass)
    }


    private fun saveStatistics(resultWorkTime: Long) {
        maxTimeWork?.let { currentMaxTimeWork ->
            if (resultWorkTime > currentMaxTimeWork) {
                maxTimeWork = resultWorkTime
            }
        }

        minTimeWork?.let { currentMinTimeWork ->
            if (resultWorkTime < currentMinTimeWork) {
                minTimeWork = resultWorkTime
            }
        }

        if (maxTimeWork == null) {
            maxTimeWork = resultWorkTime
        }

        if (minTimeWork == null) {
            minTimeWork = resultWorkTime
        }

        timeWorkList.add(resultWorkTime)
    }

    /**
     * Вывод результата тестирования
     * @param numberRun количество проведенных циклов
     */
    private fun statisticsOutput(numberRun: Int, aweraseTimeWork: Long) {
        println(
            """
            ----------------------------Программа прогона тестов----------------------------
            Проведено циклов : $numberRun
            Полученный результат 
        """.trimIndent()
        )
        println("\n maxTimeWork : $maxTimeWork ")
        println(" minTimeWork : $minTimeWork ")
        println("\n среднее значение : $aweraseTimeWork ")

    }

    /**
     * Вычисление среднего значения
     * @param numberRun количество проведенных итераций запуска функции
     * @return Long - среднее время выполнение функции
     */
    private fun aweraseTimeWork(numberRun: Int): Long {
        timeWorkList.forEach {
            aweraseTimeWork += it
        }
        if (numberRun != 0) {
            aweraseTimeWork /= numberRun
        }
        return aweraseTimeWork
    }

    /**
     * Расширение класса String для мапинга типов параметра по возвращаемому названию
     * @return тип Class или null в случае если тип не поддерживается
     */
    private fun String.toClass(): Class<out Any>? {
        return when (this) {
            "int" -> Int::class.java
            "short" -> Short::class.java
            "long" -> Long::class.java
            "float" -> Float::class.java
            "double" -> Double::class.java
            "boolean" -> Boolean::class.java
            "String" -> String::class.java
            "char" -> Char::class.java
            else -> {
                println("тип $this не поддерживается")
                null
            }
        }
    }

}

```
# Стадия готовности проекта : ГОТОВ

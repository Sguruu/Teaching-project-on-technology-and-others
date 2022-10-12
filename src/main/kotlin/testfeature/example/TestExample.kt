package testfeature.example

import testfeature.ContractTest
import java.util.*

/**
 * Пример реализации контрактка тестируемого класса
 */
class TestExample : ContractTest<TestExample> {
    override lateinit var nameMethod: String

    override fun initTest() {
        nameMethod = "testFun"
    }


    fun testFun() {
        val mockList = generateModel()
        val finalList = finalList(mockList)
        val readyList = readyList(finalList)

        println("mockList")
        mockList.forEach {
            println(it)
        }

        println("\nfinalList")
        finalList.forEach {
            println("finalList элемент массива")
            it.forEach {
                println("\t $it")
            }
        }

        println("\nreadyList")
        readyList.forEach {
            println(it)
        }
    }

    /**
     * Генерация модели данных
     * @return Список сгенерированных данных
     */
    private fun generateModel(): List<Model> {
        val mockList = mutableListOf<Model>()
        mockList.addAll(generateRandom("30-09-2022", 3))
        mockList.addAll(generateRandom("01-10-2022", 1))
        mockList.addAll(generateRandom("02-10-2022"))
        return mockList
    }

    /**
     * Группировка данных по признаку даты
     * @param list список сгенерированных данных
     * @return List<List<Model>> список группированых списков по признаку даты
     */
    private fun finalList(list: List<Model>): List<List<Model>> {
        val listMockList: MutableList<List<Model>> = mutableListOf()
        var bufferList: MutableList<Model> = mutableListOf()
        if (list.size > 1) {

            list.forEachIndexed { index, model ->
                when (index != list.size - 1) {
                    true -> {
                        if (model.data == list[index + 1].data) {
                            bufferList.add(model)
                        } else if (index != 0 && model.data == list[index - 1].data) {
                            bufferList.add(model)
                            listMockList.add(bufferList)
                            bufferList = mutableListOf()
                        } else {
                            listMockList.add(bufferList)
                            bufferList = mutableListOf()
                        }
                    }
                    false -> {
                        if (model.data == list[index - 1].data) {
                            bufferList.add(model)
                            listMockList.add(bufferList)
                        } else {
                            bufferList = mutableListOf()
                            bufferList.add(model)
                            listMockList.add(bufferList)
                        }
                    }
                }

            }

        } else {
            listMockList.add(list)
        }

        return listMockList.toList()
    }

    /**
     * Вычисление максимальной средний и минимальной средний температуры в каждой группе и вывод по одному значению от
     * каждого сгруппированного списка
     * @param value Группированный список списков по дате
     * @return List<Model> - Список со средними значениями по каждой группе
     */
    private fun readyList(value: List<List<Model>>): List<Model> {
        val readyList = mutableListOf<Model>()
        value.forEachIndexed { index, listModels ->
            val data = listModels[0].data
            var maxValue = listModels[0].maxValue
            var minValue = listModels[0].minValue
            var iterable = 1

            listModels.forEachIndexed { index, currentModel ->
                if (index != 0) {
                    iterable++
                    maxValue += currentModel.maxValue
                    minValue += currentModel.minValue
                }
            }
            readyList.add(Model(data, averageValue(maxValue, iterable), averageValue(minValue, iterable)))

        }

        return readyList
    }

    /**
     * Генерация рандомных данных
     * @param valueData Дата
     * @param valueGenerate количество сгенерированных элементов, по умолчания 5
     * @return List<Model> - список сгенерированных данных
     */
    private fun generateRandom(valueData: String, valueGenerate: Int = 5): List<Model> {
        val mockList = mutableListOf<Model>()
        val min = -40
        val max = 50
        val diff = max - min
        for (n in 0..valueGenerate) {
            val random = Random()
            var valueMax = random.nextInt(diff + 1) + min
            var valueMin = random.nextInt(diff + 1) + min

            if (valueMax < valueMin) {
                val buffer = valueMax
                valueMax = valueMin
                valueMin = buffer
            }
            mockList.add(Model(valueData, valueMax.toDouble(), valueMin.toDouble()))
        }
        return mockList
    }

    private fun averageValue(summaValue: Double, divider: Int) = summaValue / divider


    private fun doSomething() {
        var count = 0
        while (count < 200) {
            try {
                Thread.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            count++
        }
    }

    data class Model(
        val data: String, val maxValue: Double, val minValue: Double
    )
}
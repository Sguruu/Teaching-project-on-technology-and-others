package testfeature

import testfeature.example.TestExample

fun main() {
    val featureTestRun = FeatureTestRun(TestExample())
    // тут необходимо передать параметры функции в Array, если их нет поставить null
    featureTestRun.runTest(testFunctionParameters = { arrayOf("Тестовый текст", false) }, 10)
}
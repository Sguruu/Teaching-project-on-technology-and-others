package testfeature

import testfeature.example.TestExample

fun main() {
    val featureTestRun = FeatureTestRun(TestExample())
    // тут необходимо передать параметры функции в Array, если их нет поставить null
    featureTestRun.runTest(null,1_000_000_000)
}
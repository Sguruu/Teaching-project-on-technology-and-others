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
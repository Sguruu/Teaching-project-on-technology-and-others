
# Проект для тестирования фич (Version 1.0)

Проект содержит класс для тестирования фич, сюда можно подгружать протестинные фичи.

[Класс FeatureTestRun](https://github.com/Sguruu/Teaching-project-on-technology-and-others/blob/TestFeatureMain/src/main/kotlin/testfeature/FeatureTestRun.kt)

[Класс ContractTest](https://github.com/Sguruu/Teaching-project-on-technology-and-others/blob/TestFeatureMain/src/main/kotlin/testfeature/ContractTest.kt)

## Использованные источники:
[Источник](url)
##

# Важные моменты 
Реализация классов для использования теста 
<```java>
Тут вставляем способы подключения к проекту и особенности 

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

<```>
# Стадия готовности проекта : ГОТОВ

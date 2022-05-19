package mediator

import mediator.before.Button

/***
 * Реализация без паттерна медиатор
 */
fun main() {
    val button = Button()

    // Включаем фен
    button.press()

    // Выключаем фен
    button.press()

}
package mediator.after

/***
 * Класс фена
 */
class Fan {
    private val mediator = Mediator()
    var isOn = false
        private set

    /***
     * Включает питание, потом включает фен
     */
    fun turnOn() {
        mediator.start()
        isOn = true
    }

    /***
     * Отключает фен, потом отключает питание
     */
    fun turnOff() {
        isOn = false
        mediator.stop()
    }

}

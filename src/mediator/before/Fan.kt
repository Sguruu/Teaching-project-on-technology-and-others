package mediator.before

/***
 * Класс фена
 */
class Fan {
    private val powerSupplier = PowerSupplier()
    var isOn = false
        private set

    /***
     * Включает питание, потом включает фен
     */
    fun turnOn() {
        powerSupplier.turnOn()
        isOn = true
    }

    /***
     * Отключает фен, потом отключает питание
     */
    fun turnOff() {
        isOn = false
        powerSupplier.turnOff()
    }

}

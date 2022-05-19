package mediator.after

import mediator.after.Button
import mediator.after.Fan
import mediator.after.PowerSupplier

class Mediator {
    private val buttom = Button()
    private val fan = Fan()
    private val powerSupplier = PowerSupplier()

    fun press() {
        if (fan.isOn) {
            fan.turnOff()
        } else {
            fan.turnOn()
        }
    }

    fun start() {
        powerSupplier.turnOn()
    }

    fun stop() {
        powerSupplier.turnOff()
    }
}
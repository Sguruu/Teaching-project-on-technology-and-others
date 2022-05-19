package mediator.before

class Button {
    private val fan: Fan = Fan()

    fun press() {
        if (fan.isOn) {
            fan.turnOff()
        } else {
            fan.turnOn()
        }
    }
}
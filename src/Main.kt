/***
 * Тут запускаем наш код
 */
fun main() {

    /*Собираем машину с помощью строителя*/
    val car1 = BuilderCar
        .Builder(ModelCar.BMV)
        .year(2)
        .mileage(300)
        .build()

    println("car1")
    car1.outCar()

    //Сборка без строителя
    val car2 = BuilderCar(ModelCar.BMV, 2, 300, null)
}
/***
 * @property BuilderCar
 * Класс строитель автомобилей, для демонстрации использование шаблона проектирования Builder
 */
class BuilderCar
    (
    // модель - обязательный параметр
    val model: ModelCar,
    // возраст - обязательный параметр
    val year: Int,
    // пробег
    val mileage: Int,
    // примечания
    val note: String?
) {

    private constructor (builder: Builder) : this(builder.model, builder.year, builder.mileage, builder.note)

    class Builder(
        var model: ModelCar
    ) {
        var year: Int = 0
            private set
        var mileage: Int = 0
            private set
        var note: String? = null

        fun model(model: ModelCar) = apply { this.model = model }
        fun year(year: Int) = apply { this.year = year }
        fun mileage(mileage: Int) = apply { this.mileage = mileage }
        fun note(note: String) = apply { this.note = note }

        fun build() = BuilderCar(this)
    }

    fun outCar() {
        println(
            """
            $model
            $year
            $mileage
            $note
        """.trimIndent()
        )
    }
}
package builder

/***
 * @property BuilderCar
 * Класс строитель автомобилей, для демонстрации использование шаблона проектирования Builder
 * @param model модель автомобиля, сделаем его обязательным параметром в Builder.
 * @param year возраст автомобиля, сделаем его обязательным параметром в Builder.
 * @param mileage пробег
 * @param note примечания
 */
class BuilderCar
    (
    val model: ModelCar,
    val year: Int,
    val mileage: Int,
    val note: String?
) {

    // приватный конструктор для строителя
    private constructor (builder: Builder) : this(
        model = builder.model,
        year = builder.year,
        mileage = builder.mileage,
        note = builder.note
    )

    class Builder(
        var model: ModelCar
    ) {
        var year: Int = 0
            private set
        var mileage: Int = 0
            private set
        var note: String? = null

        /* Функции для присваивания */
        fun model(model: ModelCar) = apply { this.model = model }
        fun year(year: Int) = apply { this.year = year }
        fun mileage(mileage: Int) = apply { this.mileage = mileage }
        fun note(note: String) = apply { this.note = note }

        // возвращает построенный объект
        fun build() = BuilderCar(this)
    }

    // функция для вывода
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
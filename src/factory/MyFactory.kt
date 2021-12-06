package factory

// 1 Интерфейс для реализации методов
interface HostingPackageInterface {
    fun getServices(): List<String>
}

// 2 Возможные типы
enum class HostingPackageType {
    STANDARD,
    PREMIUM
}

// 3 Класс Стандартного типа
class StandardHostingPackage : HostingPackageInterface {
    override fun getServices(): List<String> = emptyList<String>().apply {
        println("StandardHostingPackage getServices")
    }
}

// 4 Класс Премиум типа
class PremiumHostingPackage : HostingPackageInterface {
    override fun getServices(): List<String> {
        println("PremiumHostingPackage getServices")
        return emptyList()
    }
}

// 5 Объект фабрики
object HostingPackageFactory {
    // 6 Функция, которая в зависимости от типа возвращает соответствующий класс
    fun getHostingFrom(type: HostingPackageType): HostingPackageInterface {
        return when (type) {
            HostingPackageType.STANDARD -> {
                StandardHostingPackage()
            }
            HostingPackageType.PREMIUM -> {
                PremiumHostingPackage()
            }
        }
    }
}

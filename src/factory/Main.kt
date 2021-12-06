package factory

fun main() {
    val myFactory1 = HostingPackageFactory.getHostingFrom(HostingPackageType.STANDARD)
    val myFactory2 = HostingPackageFactory.getHostingFrom(HostingPackageType.PREMIUM)

    myFactory1.getServices()
    myFactory2.getServices()
}
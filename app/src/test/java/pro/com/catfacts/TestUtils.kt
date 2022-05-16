package pro.com.catfacts


object TestUtils {

    fun setProperty(instance: Any, name: String, param: Any?) {

        val field = instance.javaClass.getDeclaredField(name)
        field.isAccessible = true
        field.set(instance, param)
    }
}


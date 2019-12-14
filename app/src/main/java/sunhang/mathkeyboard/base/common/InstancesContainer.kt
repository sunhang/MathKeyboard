package sunhang.mathkeyboard.base.common

import java.lang.RuntimeException
import java.util.*
import kotlin.reflect.KProperty

private val maps = WeakHashMap<Any?, MutableMap<String, Any>>()

/**
 * 应用在延迟初始化val属性的地方
 */
object InstancesContainer {

    operator fun<T> getValue(thisRef: Any?, property: KProperty<*>): T {
        val value = maps[thisRef]?.get(property.name)
        return (value as? T) ?: throw RuntimeException("Value haven't initialized")
    }

}

fun Any?.putInstanceIntoContainer(propertyName: String, instance: Any) {
    val map = maps.getOrPut(this) { mutableMapOf<String, Any>() }

    if (map[propertyName] != null) {
        throw RuntimeException("$instance has been putted")
    }

    map[propertyName] = instance
}

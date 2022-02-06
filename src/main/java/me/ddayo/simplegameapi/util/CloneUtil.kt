package me.ddayo.simplegameapi.util


class CloneUtil {
    companion object {
        fun <T> createInstance(clazz: Class<T>, vararg args: Any): T = try { clazz.getConstructor(*args.map{it::class.java}.toTypedArray()).newInstance(*args) }
        catch(e: NoSuchMethodException) { clazz.getConstructor(*args.map{ it::class.javaPrimitiveType ?: it::class.java }.toTypedArray()).newInstance(*args) }
    }
}
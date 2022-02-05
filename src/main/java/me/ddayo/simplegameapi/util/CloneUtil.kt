package me.ddayo.simplegameapi.util

import com.google.gson.Gson

class CloneUtil {
    companion object {
        val gson = Gson()
        inline fun <reified T> clone(t: T): T = gson.fromJson(gson.toJson(t), T::class.java)
        inline fun <reified T> cloneMany(t: T, s: Int): List<T> {
            val json = gson.toJson(t)
            val l = emptyList<T>().toMutableList()
            l.add(t)
            for(i in 1 until s)
                l.add(gson.fromJson(json, T::class.java))
            return l
        }

        fun <T> createInstance(clazz: Class<T>, vararg args: Any) = try { clazz.getConstructor(*args.map{it::class.java}.toTypedArray()).newInstance(*args) }
        catch(e: NoSuchMethodException) { clazz.getConstructor(*args.map{ it::class.javaPrimitiveType }.toTypedArray()).newInstance(*args) }
    }
}
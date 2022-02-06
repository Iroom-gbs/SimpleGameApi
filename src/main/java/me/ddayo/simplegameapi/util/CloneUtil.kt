package me.ddayo.simplegameapi.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.ddayo.simplegameapi.api.Game

class CloneUtil {
    companion object {
        fun <T> createInstance(clazz: Class<T>, vararg args: Any): T = try { clazz.getConstructor(*args.map{it::class.java}.toTypedArray()).newInstance(*args) }
        catch(e: NoSuchMethodException) { clazz.getConstructor(*args.map{ it::class.javaPrimitiveType }.toTypedArray()).newInstance(*args) }
    }
}
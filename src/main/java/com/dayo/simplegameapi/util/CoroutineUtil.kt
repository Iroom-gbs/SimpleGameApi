package com.dayo.simplegameapi.util

import com.dayo.simplegameapi.SimpleGameApi
import org.bukkit.Bukkit

class CoroutineUtil {
    companion object {
        public fun invokeMain(runnable: Runnable) = Bukkit.getScheduler().runTask(SimpleGameApi.instance, runnable)
    }
}
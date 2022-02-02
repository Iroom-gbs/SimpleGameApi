package com.dayo.simplegamelauncher

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class SimpleGameLauncher : JavaPlugin() {
    companion object {
        lateinit var instance: SimpleGameLauncher
        public fun getPlayer(uid: UUID): Player = instance.server.getPlayer(uid)!!
    }
    override fun onEnable() {
        instance = this
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
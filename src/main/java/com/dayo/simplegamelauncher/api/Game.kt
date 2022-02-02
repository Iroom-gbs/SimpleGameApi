package com.dayo.simplegamelauncher.api

import com.dayo.simplegamelauncher.data.GameManager
import com.dayo.simplegamelauncher.data.RoomInfo
import com.dayo.simplegamelauncher.event.GameFinishEvent
import com.dayo.simplegamelauncher.event.PlayerFailEvent
import org.bukkit.Bukkit
import java.util.*

abstract class Game {
    abstract fun onGameStart(room: RoomInfo, players: List<UUID>)
    abstract val id: Int
    abstract val name: String
    abstract val minimumPlayer: Int
    open fun onPlayerFailed(room: RoomInfo, player: UUID) {
        GameManager.leftPlayer(player)
        Bukkit.getPluginManager().callEvent(PlayerFailEvent(player, room))
    }

    open fun finish(room: RoomInfo) {
        GameManager.resetRoom(room)
        Bukkit.getPluginManager().callEvent(GameFinishEvent(room))
    }
}
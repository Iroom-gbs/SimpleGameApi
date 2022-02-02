package com.dayo.simplegameapi.api

import com.dayo.simplegameapi.data.GameManager
import com.dayo.simplegameapi.data.RoomInfo
import com.dayo.simplegameapi.event.GameFinishEvent
import com.dayo.simplegameapi.event.PlayerFailEvent
import org.bukkit.Bukkit
import java.util.*

abstract class Game {
    abstract fun onGameStart(room: RoomInfo, players: List<UUID>)
    abstract val id: Int
    abstract val name: String
    abstract val playerCount: Int
    open fun onPlayerFailed(room: RoomInfo, player: UUID) {
        GameManager.leftPlayer(player)
        Bukkit.getPluginManager().callEvent(PlayerFailEvent(player, room))
        if(GameManager.getLeftPlayer(room) == 0)
            finish(room)
    }

    open fun finish(room: RoomInfo) {
        GameManager.resetRoom(room)
        Bukkit.getPluginManager().callEvent(GameFinishEvent(room))
    }
}
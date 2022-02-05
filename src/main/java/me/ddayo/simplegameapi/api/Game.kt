package me.ddayo.simplegameapi.api

import me.ddayo.simplegameapi.data.GameManager
import me.ddayo.simplegameapi.data.RoomInfo
import me.ddayo.simplegameapi.event.GameFinishEvent
import me.ddayo.simplegameapi.event.PlayerFailEvent
import me.ddayo.simplegameapi.util.CoroutineUtil
import org.bukkit.Bukkit
import java.util.*

abstract class Game {
    abstract fun onGameStart(room: RoomInfo, players: List<UUID>)
    abstract val name: String
    abstract val playerCount: Int
    abstract val maxPlayerCount: Int

    open fun playerFailed(room: RoomInfo, player: UUID) {
        GameManager.leftPlayer(player)
        CoroutineUtil.invokeMain {
            Bukkit.getPluginManager().callEvent(PlayerFailEvent(player, room))
            if (GameManager.getCurrentPlayerCount(room) == 0)
                finish(room)
        }
    }

    open fun finish(room: RoomInfo) {
        GameManager.resetRoom(room)
        CoroutineUtil.invokeMain {
            Bukkit.getPluginManager().callEvent(GameFinishEvent(room))
        }
    }
}
package me.ddayo.simplegameapi.api

import me.ddayo.simplegameapi.data.GameManager
import me.ddayo.simplegameapi.data.RoomInfo
import me.ddayo.simplegameapi.event.GameFinishEvent
import me.ddayo.simplegameapi.event.PlayerFailEvent
import me.ddayo.simplegameapi.util.CoroutineUtil
import org.bukkit.Bukkit
import java.util.*

abstract class Game {
    abstract fun onGameStart(players: List<UUID>)
    abstract val name: String
    abstract val playerCount: Int
    abstract val maxPlayerCount: Int
    protected lateinit var room: RoomInfo private set

    internal fun setRoom(room: RoomInfo) {
        this.room = room
    }

    open fun playerFailed(player: UUID) {
        GameManager.leftPlayer(player)
        CoroutineUtil.invokeMain {
            Bukkit.getPluginManager().callEvent(PlayerFailEvent(player, room))
            if (GameManager.getCurrentPlayerCount(room) == 0)
                finish()
        }
    }

    open fun finish() {
        val cp = GameManager.getPlayersInRoom(room)!!.toList()
        for(player in cp) {
            CoroutineUtil.invokeMain {
                Bukkit.getPluginManager().callEvent(PlayerFailEvent(player, room))
            }
            GameManager.leftPlayer(player)
        }
        GameManager.finalizeRoom(room)
        CoroutineUtil.invokeMain {
            Bukkit.getPluginManager().callEvent(GameFinishEvent(room))
        }
    }
}
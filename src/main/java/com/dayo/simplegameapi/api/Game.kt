package com.dayo.simplegameapi.api

import com.dayo.simplegameapi.data.GameManager
import com.dayo.simplegameapi.data.RoomInfo
import com.dayo.simplegameapi.event.GameFinishEvent
import com.dayo.simplegameapi.event.PlayerFailEvent
import com.dayo.simplegameapi.util.CoroutineUtil
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
        CoroutineUtil.invokeMain {
            GameManager.resetRoom(room)
            Bukkit.getPluginManager().callEvent(GameFinishEvent(room))
        }
    }
}
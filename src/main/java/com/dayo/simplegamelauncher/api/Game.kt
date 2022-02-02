package com.dayo.simplegamelauncher.api

import com.dayo.simplegamelauncher.data.GameManager
import com.dayo.simplegamelauncher.data.RoomInfo
import java.util.*

abstract class Game {
    abstract fun onGameStart(room: RoomInfo, players: List<UUID>)
    abstract val id: Int
    abstract val name: String
    abstract val minimumPlayer: Int
    open fun onPlayerFailed(room: RoomInfo, player: UUID) {
        GameManager.leftPlayer(player)
        GameManager.onPlayerFailed(player, room)
    }

    open fun finish(room: RoomInfo) {
        GameManager.resetRoom(room)
        GameManager.onGameFinished(room)
    }
}
package com.dayo.simplegamelauncher.data

import java.util.*

abstract class Game {
    abstract fun onGameStart(room: RoomInfo, players: List<UUID>)
    abstract val id: Int
    abstract val name: String
    abstract val minimumPlayer: Int
    fun onPlayerFailed(room: RoomInfo, player: UUID) {
        GameManager.leftPlayer(player)
        GameManager.onPlayerFailed(player, room)
    }

    fun finish(room: RoomInfo) {
        GameManager.resetRoom(room)
        GameManager.onGameFinished(room)
    }
}
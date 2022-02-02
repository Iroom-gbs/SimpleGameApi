package com.dayo.simplegamelauncher.data

import java.util.*

open class GameEventListener {
    fun onPlayerFailed(uid: UUID, room: RoomInfo) {}
    fun onGameFinished(room: RoomInfo) {}
}
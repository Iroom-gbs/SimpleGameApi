package com.dayo.simplegamelauncher.data

import java.util.*

interface GameEventListener {
    fun onPlayerFailed(uid: UUID, room: RoomInfo) {}
    fun onGameFinished(room: RoomInfo) {}
}
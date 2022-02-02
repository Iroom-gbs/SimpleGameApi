package com.dayo.simplegamelauncher.api

import com.dayo.simplegamelauncher.data.RoomInfo
import java.util.*

interface GameEventListener {
    fun onPlayerFailed(uid: UUID, room: RoomInfo) {}
    fun onGameFinished(room: RoomInfo) {}
}
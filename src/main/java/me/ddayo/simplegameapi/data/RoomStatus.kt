package me.ddayo.simplegameapi.data

import java.util.*

data class RoomStatus(public val players: MutableList<UUID>, public var status: Status)

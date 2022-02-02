package com.dayo.simplegameapi.data

import org.bukkit.entity.Player
import java.util.*

data class RoomStatus(public val players: MutableList<UUID>, public var status: Status)

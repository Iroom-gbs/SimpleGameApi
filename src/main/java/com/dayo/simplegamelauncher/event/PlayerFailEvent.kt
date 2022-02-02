package com.dayo.simplegamelauncher.event

import com.dayo.simplegamelauncher.data.RoomInfo
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import java.util.*

class PlayerFailEvent(uid: UUID, roominfo: RoomInfo) : PlayerEvent(Bukkit.getPlayer(uid)!!) {
    companion object
    {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() : HandlerList
        {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    private var roominfo : RoomInfo = roominfo
        get() {
            return roominfo
        }

    private var uuid : UUID = uid
        get() {
            return uuid
        }
}
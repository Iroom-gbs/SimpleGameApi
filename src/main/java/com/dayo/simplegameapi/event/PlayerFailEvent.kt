package com.dayo.simplegameapi.event

import com.dayo.simplegameapi.data.RoomInfo
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import java.util.*

class PlayerFailEvent(uid: UUID, roomInfo: RoomInfo) : PlayerEvent(Bukkit.getPlayer(uid)!!) {
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

    private val roomInfo : RoomInfo
        get() {
            return roomInfo
        }

    private val uuid : UUID
        get() {
            return uuid
        }
}
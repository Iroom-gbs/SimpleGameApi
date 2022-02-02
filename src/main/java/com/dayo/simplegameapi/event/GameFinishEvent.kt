package com.dayo.simplegameapi.event

import com.dayo.simplegameapi.data.RoomInfo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList


class GameFinishEvent(roominfo: RoomInfo) : Event() {
    companion object
    {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList
        {
            return HANDLERS
        }
    }

    override fun getHandlers() : HandlerList
    {
        return HANDLERS
    }

    private var roominfo : RoomInfo = roominfo
        get()
    {
        return roominfo
    }
}
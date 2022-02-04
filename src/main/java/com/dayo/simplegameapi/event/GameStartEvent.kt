package com.dayo.simplegameapi.event

import com.dayo.simplegameapi.data.RoomInfo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList


class GameStartEvent(roomInfo: RoomInfo) : Event() {
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

    private val roomInfo : RoomInfo
        get()
    {
        return roomInfo
    }
}
package me.ddayo.simplegameapi.event

import me.ddayo.simplegameapi.data.RoomInfo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class GameFinishEvent(public val roomInfo: RoomInfo) : Event() {
    companion object
    {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

    override fun getHandlers() = HANDLERS
}
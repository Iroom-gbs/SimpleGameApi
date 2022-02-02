package com.dayo.simplegamelauncher.data

import com.dayo.simplegamelauncher.SimpleGameLauncher
import com.dayo.simplegamelauncher.api.Game
import kotlinx.coroutines.*
import org.bukkit.entity.Player
import java.util.*

class GameManager {
    companion object {
        val gameList = emptyMap<Int, Game>().toMutableMap()
        val playerStatus = emptyMap<UUID, RoomInfo?>().toMutableMap()
        val roomStatus = emptyMap<RoomInfo, MutableList<UUID>>().toMutableMap()

        public fun joinPlayer(uid: UUID, room: RoomInfo): Boolean {
            playerStatus[uid]?.let {
                roomStatus[it]!!.remove(uid)
            }
            playerStatus[uid] = room
            roomStatus[room]!!.add(uid)
            if(roomStatus[room]!!.size == gameList[room.gid]!!.minimumPlayer) {
                CoroutineScope(Dispatchers.Default).launch {
                    for(t in 0 until 10) {
                        SimpleGameLauncher.getPlayer(uid).sendMessage("${10 - t}초 후 시작합니다!")
                        delay(1000)
                        if(roomStatus[room]!!.size < gameList[room.gid]!!.minimumPlayer)
                            return@launch
                    }
                    gameList[room.gid]?.onGameStart(room, roomStatus[room]!!)
                }
            }
            return true
        }

        public fun leftPlayer(uid: UUID) {
            playerStatus[uid]?.let {
                roomStatus[it]!!.remove(uid)
            }
            playerStatus[uid] = null
        }

        public fun resetRoom(room: RoomInfo) {
            while(roomStatus[room]!!.size > 0) leftPlayer(roomStatus[room]!![roomStatus[room]!!.size - 1])
        }

        public fun getPlaying(p: Player): RoomInfo? {
            return playerStatus[p.uniqueId]
        }

        /*
        public fun registerGame(game: Game) {
            if(gameList.containsKey(game.id))
                throw IllegalArgumentException("Game id ${game.id} already exists")
            gameList[game.id] = game
        }
         */

        public fun registerGame(game: Game, roomSize: Int) {
            if(gameList.containsKey(game.id))
                throw IllegalArgumentException("Game id ${game.id} already exists")
            gameList[game.id] = game
            for(i in 0 until roomSize)
                roomStatus[RoomInfo(game.id, i)] = emptyList<UUID>().toMutableList()
        }

        public fun getGameById(game: Int): Game = gameList[game]!!
    }
}
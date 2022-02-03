package com.dayo.simplegameapi.data

import com.dayo.simplegameapi.SimpleGameApi
import com.dayo.simplegameapi.api.Game
import kotlinx.coroutines.*
import org.bukkit.entity.Player
import java.util.*

class GameManager {
    companion object {
        private val gameList = emptyMap<Int, Game>().toMutableMap()
        private val playerStatus = emptyMap<UUID, RoomInfo?>().toMutableMap()
        private val roomStatus = emptyMap<RoomInfo, RoomStatus>().toMutableMap()

        public fun joinPlayer(uid: UUID, room: RoomInfo): Boolean {
            playerStatus[uid]?.let {
                roomStatus[it]!!.players.remove(uid)
            }
            playerStatus[uid] = room
            roomStatus[room]!!.players.add(uid)
            if(roomStatus[room]!!.players.size == gameList[room.gid]!!.playerCount && roomStatus[room]!!.status == Status.Waiting) {
                CoroutineScope(Dispatchers.Default).launch {
                    roomStatus[room]!!.status = Status.Pending
                    for(t in 0 until 10) {
                        roomStatus[room]!!.players.forEach{SimpleGameApi.getPlayer(it).sendMessage("${10 - t}초 후 시작합니다!")}
                        delay(1000)
                        if(roomStatus[room]!!.players.size < gameList[room.gid]!!.playerCount) {
                            roomStatus[room]!!.status = Status.Waiting
                            return@launch
                        }
                    }
                    roomStatus[room]!!.status = Status.Playing
                    gameList[room.gid]?.onGameStart(room, roomStatus[room]!!.players)
                }
            }
            return true
        }

        public fun leftPlayer(uid: UUID) {
            playerStatus[uid]?.let {
                roomStatus[it]!!.players.remove(uid)
            }
            playerStatus[uid] = null
        }

        public fun resetRoom(room: RoomInfo) {
            while(roomStatus[room]!!.players.size > 0) leftPlayer(roomStatus[room]!!.players[roomStatus[room]!!.players.size - 1])
            roomStatus[room]!!.status = Status.Waiting
        }

        public fun getPlaying(p: Player): RoomInfo? {
            return playerStatus[p.uniqueId]
        }

        public fun getRoomStatus(room: RoomInfo): Status = roomStatus[room]!!.status

        public fun getLeftPlayer(room: RoomInfo): Int = roomStatus[room]!!.players.size

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
                roomStatus[RoomInfo(game.id, i)] = RoomStatus(emptyList<UUID>().toMutableList(), Status.Waiting)
        }

        public fun getGameById(game: Int): Game = gameList[game]!!
    }
}
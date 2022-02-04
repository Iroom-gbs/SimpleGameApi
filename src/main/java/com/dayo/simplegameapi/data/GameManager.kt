package com.dayo.simplegameapi.data

import com.dayo.simplegameapi.SimpleGameApi
import com.dayo.simplegameapi.api.Game
import com.dayo.simplegameapi.util.CoroutineUtil
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.Runnable
import java.util.*

class GameManager {
    companion object {
        private val gameList = emptyList<Game>().toMutableList()
        private val playerStatus = emptyMap<UUID, RoomInfo?>().toMutableMap()
        private val roomStatus = emptyMap<RoomInfo, RoomStatus>().toMutableMap()
        private val idList = emptyMap<String, Int>().toMutableMap()

        public fun joinPlayer(uid: UUID, room: RoomInfo): Boolean {
            if(roomStatus[room]!!.players.size >= gameList[room.gid].maxPlayerCount) {
                SimpleGameApi.getPlayer(uid).sendMessage("이미 최대 인원입니다.")
                return false
            }
            if(getPlaying(uid)?.let { getRoomStatus(it) } == Status.Playing) {
                SimpleGameApi.getPlayer(uid).sendMessage("이미 게임에 참가중입니다.")
                return false
            }
            playerStatus[uid]?.let {
                roomStatus[it]!!.players.remove(uid)
            }
            playerStatus[uid] = room
            roomStatus[room]!!.players.add(uid)
            if(roomStatus[room]!!.players.size == gameList[room.gid].playerCount && roomStatus[room]!!.status == Status.Waiting) {
                CoroutineScope(Dispatchers.Default).launch {
                    roomStatus[room]!!.status = Status.Pending
                    for(t in 0 until 10) {
                        roomStatus[room]!!.players.forEach{SimpleGameApi.getPlayer(it).sendMessage("${10 - t}초 후 시작합니다!")}
                        delay(1000)
                        if(roomStatus[room]!!.players.size < gameList[room.gid].playerCount) {
                            roomStatus[room]!!.status = Status.Waiting
                            return@launch
                        }
                    }
                    roomStatus[room]!!.status = Status.Playing
                    CoroutineUtil.invokeMain {
                        gameList[room.gid].onGameStart(room, roomStatus[room]!!.players)
                    }
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

        public fun getPlaying(p: UUID): RoomInfo? {
            return playerStatus[p]
        }

        public fun getRoomStatus(room: RoomInfo): Status = roomStatus[room]!!.status

        public fun getCurrentPlayerCount(room: RoomInfo): Int = roomStatus[room]!!.players.size

        public fun getGameId(name: String): Int? = idList[name]
        public fun getGameId(game: Game): Int? = idList[game.name]

        /*
        public fun registerGame(game: Game) {
            if(gameList.containsKey(game.id))
                throw IllegalArgumentException("Game id ${game.id} already exists")
            gameList[game.id] = game
        }
         */

        public fun registerGame(game: Game, roomSize: Int) {
            if(idList.containsKey(game.name))
                throw IllegalArgumentException("Game ${game.name} already exists")
            idList[game.name] = gameList.size
            gameList.add(game)
            for(i in 0 until roomSize)
                roomStatus[RoomInfo(idList[game.name]!!, i)] = RoomStatus(emptyList<UUID>().toMutableList(), Status.Waiting)
        }

        public fun getGameById(game: Int): Game = gameList[game]
    }
}
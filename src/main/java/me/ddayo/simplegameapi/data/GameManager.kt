package me.ddayo.simplegameapi.data

import me.ddayo.coroutine.Coroutine
import me.ddayo.coroutine.functions.WaitSeconds
import me.ddayo.simplegameapi.SimpleGameApi
import me.ddayo.simplegameapi.api.Game
import me.ddayo.simplegameapi.event.GameStartEvent
import me.ddayo.simplegameapi.event.PlayerFailEvent
import me.ddayo.simplegameapi.util.CoroutineUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.Runnable
import java.util.*
import kotlin.reflect.KClass

class GameManager {
    companion object {
        private val gameList = emptyMap<RoomInfo, Game>().toMutableMap()
        private val playerStatus = emptyMap<UUID, RoomInfo?>().toMutableMap()
        private val roomStatus = emptyMap<RoomInfo, RoomStatus>().toMutableMap()
        private val idList = emptyMap<String, Int>().toMutableMap()

        public fun joinPlayer(uid: UUID, room: RoomInfo): Boolean {
            if(roomStatus[room]!!.players.size >= gameList[room]!!.maxPlayerCount) {
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
            if(roomStatus[room]!!.players.size == gameList[room]!!.playerCount && roomStatus[room]!!.status == Status.Waiting) {
                Coroutine.startCoroutine(sequence {
                    roomStatus[room]!!.status = Status.Pending
                    for(t in 0 until 10) {
                        roomStatus[room]!!.players.forEach{ SimpleGameApi.getPlayer(it).sendMessage("${10 - t}초 후 시작합니다!")}
                        yield(WaitSeconds(1.0))
                        if(roomStatus[room]!!.players.size < gameList[room]!!.playerCount) {
                            roomStatus[room]!!.status = Status.Waiting
                            return@sequence
                        }
                    }
                    roomStatus[room]!!.status = Status.Playing
                    CoroutineUtil.invokeMain {
                        Bukkit.getPluginManager().callEvent(GameStartEvent(room))
                        gameList[room]!!.onGameStart(room, roomStatus[room]!!.players)
                    }
                })
            }
            return true
        }

        public fun leftPlayer(uid: UUID) {
            playerStatus[uid]?.let {
                roomStatus[it]!!.players.remove(uid)
            }
            playerStatus[uid] = null
        }

        public fun makePlayerFailed(uid: UUID): Boolean {
            val room = getPlaying(uid)
            if(room == null) {
                return false
            }
            else getGame(room)!!.playerFailed(room, uid)
            return true
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

        public fun registerGame(game: Game, roomSize: Int) {

            if(idList.containsKey(game.name))
                throw IllegalArgumentException("Game ${game.name} already exists")
            idList[game.name] = idList.size
            println("Game ${game.name} registered to id: ${idList[game.name]} with $roomSize rooms")
            //gameList.add(game)
            for(i in 0 until roomSize) {
                gameList[RoomInfo(idList[game.name]!!, i)] = game
                roomStatus[RoomInfo(idList[game.name]!!, i)] = RoomStatus(emptyList<UUID>().toMutableList(), Status.Waiting)
            }
        }

        public fun getGame(game: RoomInfo): Game? = gameList[game]
    }
}
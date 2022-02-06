package me.ddayo.simplegameapi.data

data class RoomInfo (val gid: Int, val rid: Int) {
    constructor(name: String, rid: Int): this(GameManager.getGameId(name)!!, rid)
}

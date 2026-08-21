package dev.slne.surf.lobby.core.client.redis

import dev.slne.surf.redis.RedisApi

val lobbyRedisLoader = LobbyRedisLoader()
val lobbyRedisApi get() = lobbyRedisLoader.redisApi

class LobbyRedisLoader {
    lateinit var redisApi: RedisApi

    fun onLoad() {
        redisApi = RedisApi.create()
    }

    fun onEnable() {
        redisApi.freezeAndConnect()
    }

    fun disconnect() {
        redisApi.disconnect()
    }
}

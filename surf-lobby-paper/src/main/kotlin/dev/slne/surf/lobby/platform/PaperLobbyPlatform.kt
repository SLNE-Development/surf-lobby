package dev.slne.surf.lobby.platform

import com.github.shynixn.mccoroutine.folia.launch
import com.google.auto.service.AutoService
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.lobby.plugin
import kotlinx.coroutines.CoroutineScope
import org.bukkit.Bukkit
import java.util.*

@AutoService(LobbyPlatform::class)
class PaperLobbyPlatform : LobbyPlatform {

    override val dataPath get() = plugin.dataPath

    override val defaultWorldName: String get() = Bukkit.getWorlds().first().name

    override val parkourAvailable get() = plugin.checkParkourHook()
    override val trophyAvailable get() = plugin.checkTrophyHook()
    override val profileAvailable get() = plugin.checkProfileHook()
    override val settingsAvailable get() = plugin.checkSettingsHook()

    override fun hasPermission(playerUuid: UUID, permission: String) =
        Bukkit.getPlayer(playerUuid)?.hasPermission(permission) == true

    override fun audience(playerUuid: UUID) = Bukkit.getPlayer(playerUuid)

    override fun launch(block: suspend CoroutineScope.() -> Unit) = plugin.launch(block = block)
}

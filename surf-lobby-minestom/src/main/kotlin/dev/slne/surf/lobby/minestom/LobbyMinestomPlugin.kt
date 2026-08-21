package dev.slne.surf.lobby.minestom

import com.google.auto.service.AutoService
import dev.slne.minestom.lobby.api.plugin.MinestomPlugin
import dev.slne.minestom.lobby.api.plugin.annotation.MinestomPluginMeta
import dev.slne.surf.lobby.minestom.command.LobbyCommandRegistrar
import dev.slne.surf.lobby.minestom.listener.DoubleJumpListener
import dev.slne.surf.lobby.minestom.listener.HotbarItemListener
import dev.slne.surf.lobby.minestom.listener.LobbyPlayerListener
import dev.slne.surf.lobby.minestom.listener.MovementListener
import dev.slne.surf.lobby.minestom.listener.PushbackAttackListener
import dev.slne.surf.lobby.minestom.listener.WorldProtectionListener

@AutoService(MinestomPlugin::class)
@MinestomPluginMeta(
    "surf-lobby-minestom",
    dependsOn = [
        "surf-api-minestom",
        "surf-redis-minestom",
        "surf-core-minestom",
        "surf-queue-minestom",
        "surf-parkour-minestom",
        "surf-profile-minestom",
        "surf-settings-minestom",
        "surf-trophy-minestom"
    ]
)
class LobbyMinestomPlugin : MinestomPlugin(LobbyMinestomEntrypoint::class.java) {
    override fun configurePlugin() {
        bindEventRegistrar<LobbyPlayerListener>()
        bindEventRegistrar<DoubleJumpListener>()
        bindEventRegistrar<MovementListener>()
        bindEventRegistrar<HotbarItemListener>()
        bindEventRegistrar<WorldProtectionListener>()
        bindEventRegistrar<PushbackAttackListener>()
        bindCommandRegistrar<LobbyCommandRegistrar>()
    }
}

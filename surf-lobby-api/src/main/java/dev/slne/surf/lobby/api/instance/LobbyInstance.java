package dev.slne.surf.lobby.api.instance;

import dev.slne.surf.lobby.api.player.LobbyPlayerManager;
import dev.slne.surf.lobby.api.queue.ServerQueueRegistry;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.springframework.context.ConfigurableApplicationContext;

@NonExtendable
public interface LobbyInstance {

  ConfigurableApplicationContext getApplicationContext();

  LobbyPlayerManager getPlayerManager();

  ServerQueueRegistry getQueueRegistry();
}
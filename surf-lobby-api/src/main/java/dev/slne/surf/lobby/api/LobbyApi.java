package dev.slne.surf.lobby.api;

import dev.slne.surf.lobby.api.player.LobbyPlayer;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import dev.slne.surf.lobby.api.instance.LobbyInstance;

import static com.google.common.base.Preconditions.*;

public final class LobbyApi {

  private static LobbyInstance instance;

  public LobbyApi(LobbyInstance instance) {
    checkState(LobbyApi.instance == null, "Instance already exists");

    LobbyApi.instance = instance;
  }

  public static LobbyInstance getInstance() {
    return instance;
  }

  public static LobbyPlayer getPlayer(@NotNull UUID uuid) {
    return instance.getPlayerManager().getPlayer(uuid);
  }

  public static ConfigurableApplicationContext getContext() {
    return instance.getApplicationContext();
  }
}

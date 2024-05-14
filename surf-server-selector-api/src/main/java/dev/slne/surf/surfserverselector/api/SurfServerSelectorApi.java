package dev.slne.surf.surfserverselector.api;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import dev.slne.surf.surfserverselector.api.instance.SurfServerSelectorInstance;

import static com.google.common.base.Preconditions.*;

public final class SurfServerSelectorApi {

  private static SurfServerSelectorInstance instance;

  public SurfServerSelectorApi(SurfServerSelectorInstance instance) {
    checkState(SurfServerSelectorApi.instance == null, "Instance already exists");

    SurfServerSelectorApi.instance = instance;
  }

  public static SurfServerSelectorInstance getInstance() {
    return instance;
  }

  public static ServerSelectorPlayer getPlayer(@NotNull UUID uuid) {
    return instance.getPlayerManager().getPlayer(uuid);
  }

  public static ConfigurableApplicationContext getContext() {
    return instance.getApplicationContext();
  }
}

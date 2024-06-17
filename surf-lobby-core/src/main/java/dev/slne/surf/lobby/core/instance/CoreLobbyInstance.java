package dev.slne.surf.lobby.core.instance;

import dev.slne.surf.lobby.api.instance.LobbyInstance;
import dev.slne.surf.lobby.api.player.LobbyPlayerManager;
import dev.slne.surf.lobby.api.queue.ServerQueueRegistry;
import dev.slne.surf.lobby.core.player.CoreLobbyPlayerManager;
import dev.slne.surf.lobby.core.spring.SurfLobbySpringApplication;
import java.nio.file.Path;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class CoreLobbyInstance implements LobbyInstance {

  protected static final ComponentLogger LOGGER = ComponentLogger.logger("lobbyInstance");
  private final CoreLobbyPlayerManager playerManager;
  private final ServerQueueRegistry queueRegistry;

  private ConfigurableApplicationContext context;

  public CoreLobbyInstance(CoreLobbyPlayerManager playerManager, ServerQueueRegistry queueRegistry) {
    this.playerManager = playerManager;
    this.queueRegistry = queueRegistry;
  }

  @OverridingMethodsMustInvokeSuper
  public void onLoad() {
    this.context = SurfLobbySpringApplication.run(getClassLoader());
  }

  @OverridingMethodsMustInvokeSuper
  public void onEnable() {
  }

  @OverridingMethodsMustInvokeSuper
  public void onDisable() {
    this.context.close();
  }

  @Override
  public ConfigurableApplicationContext getApplicationContext() {
    return this.context;
  }

  @Override
  public LobbyPlayerManager getPlayerManager() {
    return this.playerManager;
  }

  @Override
  public ServerQueueRegistry getQueueRegistry() {
    return this.queueRegistry;
  }

  protected abstract ClassLoader getClassLoader();

  protected abstract Path getDataFolder();
}
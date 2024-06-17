package dev.slne.surf.lobby.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.velocity.instance.VelocityLobbyInstance;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;

@Plugin(
    id = "surf-lobby-velocity",
    version = "1.0.0-SNAPSHOT",
    description = "A plugin that allows players to select a server to connect to.",
    dependencies = {
        @Dependency(id = "luckperms"),
        @Dependency(id = "surf-api-velocity"),
        @Dependency(id = "surf-data-velocity")
    }
)
public final class VelocityMain {

  private static VelocityMain mainInstance;

  private final ProxyServer server;
  private final Logger logger;
  private final PluginContainer pluginContainer;
  private final Path dataDirectory;
  private final ExecutorService executorService;
  private final VelocityLobbyInstance instance;

  @Inject
  public VelocityMain(ProxyServer server, Logger logger, PluginContainer pluginContainer,
    @DataDirectory Path dataDirectory, ExecutorService executorService) {
    VelocityMain.mainInstance = this;

    this.server = server;
    this.logger = logger;
    this.pluginContainer = pluginContainer;
    this.dataDirectory = dataDirectory;
    this.executorService = executorService;

    this.instance = new VelocityLobbyInstance();
    new LobbyApi(this.instance);

    this.instance.onLoad();
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    this.instance.onEnable();
  }

  @Subscribe
  public void onProxyShutdown(ProxyShutdownEvent event) {
    this.instance.onDisable();
  }

  public ProxyServer getServer() {
      return server;
  }

  public Logger getLogger() {
      return logger;
  }

  public PluginContainer getPluginContainer() {
      return pluginContainer;
  }

  public Path getDataDirectory() {
      return dataDirectory;
  }

  public ExecutorService getExecutorService() {
      return executorService;
  }

  public ClassLoader getClassLoader() {
    return getClass().getClassLoader();
  }

  public static VelocityMain getInstance() {
    return VelocityMain.mainInstance;
  }
}
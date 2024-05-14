package dev.slne.surf.surfserverselector.core.instance;

import dev.slne.surf.surfserverselector.api.instance.SurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.core.spring.SurfServerSelectorSpringApplication;
import java.nio.file.Path;
import org.springframework.context.ConfigurableApplicationContext;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class CoreSurfServerSelectorInstance implements SurfServerSelectorInstance {

  protected static final ComponentLogger LOGGER = ComponentLogger.logger("SurfServerSelectorInstance");
  private final CoreServerSelectorPlayerManager playerManager;

  private ConfigurableApplicationContext context;

  public CoreSurfServerSelectorInstance(CoreServerSelectorPlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @OverridingMethodsMustInvokeSuper
  public void onLoad() {
    this.context = SurfServerSelectorSpringApplication.run(getClassLoader());
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
  public ServerSelectorPlayerManager getPlayerManager() {
    return this.playerManager;
  }

  protected abstract ClassLoader getClassLoader();

  protected abstract Path getDataFolder();
}
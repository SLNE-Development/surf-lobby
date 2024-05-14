package dev.slne.surf.surfserverselector.core.instance;

import dev.slne.surf.surfserverselector.api.instance.SurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.core.spring.SurfServerSelectorSpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class CoreSurfServerSelectorInstance implements SurfServerSelectorInstance {

  protected static final ComponentLogger LOGGER = ComponentLogger.logger("SurfServerSelectorInstance");

  private ConfigurableApplicationContext context;

  public CoreSurfServerSelectorInstance() {
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

  protected abstract ClassLoader getClassLoader();
}
package dev.slne.surf.surfserverselector.api.instance;

import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.springframework.context.ConfigurableApplicationContext;

@NonExtendable
public interface SurfServerSelectorInstance {

  ConfigurableApplicationContext getApplicationContext();
}
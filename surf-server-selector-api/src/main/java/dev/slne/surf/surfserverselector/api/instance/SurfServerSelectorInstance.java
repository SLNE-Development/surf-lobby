package dev.slne.surf.surfserverselector.api.instance;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.api.queue.ServerQueueRegistry;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.springframework.context.ConfigurableApplicationContext;

@NonExtendable
public interface SurfServerSelectorInstance {

  ConfigurableApplicationContext getApplicationContext();

  ServerSelectorPlayerManager getPlayerManager();

  ServerQueueRegistry getQueueRegistry();
}
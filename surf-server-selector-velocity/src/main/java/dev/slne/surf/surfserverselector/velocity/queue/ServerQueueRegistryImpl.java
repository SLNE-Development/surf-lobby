package dev.slne.surf.surfserverselector.velocity.queue;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import dev.slne.surf.surfserverselector.api.queue.ServerQueueRegistry;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ServerQueueRegistryImpl implements ServerQueueRegistry {
  private final LoadingCache<String, ServerQueue> queues;

  public ServerQueueRegistryImpl() {
    queues = Caffeine.newBuilder()
        .build(this::createQueue);
  }

  @Override
  public ServerQueue getQueue(@NotNull String serverName) {
    return queues.get(serverName);
  }

  @Override
  public void removeFromQueue(@NotNull UUID uuid) {
    queues.asMap().values().forEach(queue -> queue.removeFromQueue(uuid));
  }

  @Contract(value = "_ -> new", pure = true)
  private @NotNull ServerQueue createQueue(String serverName) {
    return new ServerQueueImpl();
  }
}

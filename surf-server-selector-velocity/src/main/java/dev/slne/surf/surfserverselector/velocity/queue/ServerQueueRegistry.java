package dev.slne.surf.surfserverselector.velocity.queue;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ServerQueueRegistry {

  public static final ServerQueueRegistry INSTANCE = new ServerQueueRegistry();

  private final LoadingCache<String, ServerQueue> queues;

  private ServerQueueRegistry() {
    queues = Caffeine.newBuilder()
        .build(this::createQueue);
  }

  public ServerQueue getQueue(String serverName) {
    return queues.get(serverName);
  }

  @Contract(value = "_ -> new", pure = true)
  private @NotNull ServerQueue createQueue(String serverName) {
    return new ServerQueueImpl();
  }
}

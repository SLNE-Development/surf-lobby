package dev.slne.surf.lobby.velocity.queue;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.api.player.LobbyPlayer;
import dev.slne.surf.lobby.api.queue.ServerQueue;
import dev.slne.surf.lobby.api.queue.ServerQueueRegistry;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Override
  public @NotNull Optional<ServerQueue> getCurrentQueue(@NotNull UUID uuid) {
    return queues.asMap().values().stream()
        .filter(queue -> queue.isInQueue(uuid))
        .findFirst();
  }

  @Override
  public Map<UUID, ServerQueue> getQueues() {
    return queues.asMap().values()
        .stream()
        .flatMap(serverQueue -> serverQueue.getPlayersInQueue().stream()
            .map(uuid -> Map.entry(uuid, serverQueue)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public Map<LobbyPlayer, ServerQueue> getPlayerQueues() {
    return getQueues().entrySet().stream()
        .collect(Collectors.toMap(entry -> LobbyApi.getPlayer(entry.getKey()),
            Map.Entry::getValue));
  }

  @Contract(value = "_ -> new", pure = true)
  private @NotNull ServerQueue createQueue(String serverName) {
    return new ServerQueueImpl(serverName);
  }
}

package dev.slne.surf.surfserverselector.core.spring.redis.events.server.change;

import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;
import java.util.UUID;
import org.jetbrains.annotations.Contract;

public class RequestChangeServerEvent extends ServerSelectorRedisEvent {

  public static final String CHANNEL = "surf:server-selector:change-server";
  private UUID uuid;
  private String requestedServerName;
  private boolean sendFeedback;
  private boolean fallbackToLobbyWithLowestPlayerCount;

  public RequestChangeServerEvent() {
  }

  public RequestChangeServerEvent(UUID uuid, String requestedServerName, boolean sendFeedback, boolean fallbackToLobbyWithLowestPlayerCount) {
    super(CHANNEL);
    this.uuid = uuid;
    this.requestedServerName = requestedServerName;
    this.sendFeedback = sendFeedback;
    this.fallbackToLobbyWithLowestPlayerCount = fallbackToLobbyWithLowestPlayerCount;
  }

  @Contract(pure = true)
  public UUID getUuid() {
    return uuid;
  }

  @Contract(pure = true)
  public String getRequestedServerName() {
    return requestedServerName;
  }

  public boolean isSendFeedback() {
    return sendFeedback;
  }

  public boolean isFallbackToLobbyWithLowestPlayerCount() {
    return fallbackToLobbyWithLowestPlayerCount;
  }
}

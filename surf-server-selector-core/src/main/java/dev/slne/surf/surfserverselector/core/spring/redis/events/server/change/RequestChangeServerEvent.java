package dev.slne.surf.surfserverselector.core.spring.redis.events.server.change;

import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;
import java.util.UUID;
import org.jetbrains.annotations.Contract;

public final class RequestChangeServerEvent extends ServerSelectorRedisEvent {

  public static final String CHANNEL = "surf:server-selector:change-server";
  private UUID uuid;
  private String requestedServerName;
  private boolean sendFeedback;

  public RequestChangeServerEvent() {
  }

  public RequestChangeServerEvent(UUID uuid, String requestedServerName, boolean sendFeedback) {
    super(CHANNEL);
    this.uuid = uuid;
    this.requestedServerName = requestedServerName;
    this.sendFeedback = sendFeedback;
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
}

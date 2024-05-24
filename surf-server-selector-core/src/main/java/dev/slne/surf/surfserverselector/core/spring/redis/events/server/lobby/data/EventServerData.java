package dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data;

public class EventServerData {
  private String eventServerName = "";
  private boolean eventServerEnabled = false;
  private int maxPlayers = 0, onlinePlayers = 0;

  public EventServerData() {
  }

  public EventServerData(String eventServerName, boolean eventServerEnabled, int maxPlayers, int onlinePlayers) {
    this.eventServerName = eventServerName;
    this.eventServerEnabled = eventServerEnabled;
    this.maxPlayers = maxPlayers;
    this.onlinePlayers = onlinePlayers;
  }

  public static EventServerData of(String eventServerName, boolean eventServerEnabled, int maxPlayers, int onlinePlayers) {
    return new EventServerData(eventServerName, eventServerEnabled, maxPlayers, onlinePlayers);
  }

  public boolean isEventServerEnabled() {
    return eventServerEnabled;
  }

  public String getEventServerName() {
    return eventServerName;
  }

  public int getOnlinePlayers() {
    return onlinePlayers;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }
}

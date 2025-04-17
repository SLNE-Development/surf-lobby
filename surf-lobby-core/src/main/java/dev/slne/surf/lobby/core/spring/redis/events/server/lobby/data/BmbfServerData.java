package dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data;

public class BmbfServerData {
  private String bmbfServerName = "";
  private boolean enabled = false;
  private int maxPlayers = 0, onlinePlayers = 0;

  public BmbfServerData() {
  }

  public BmbfServerData(String bmbfServerName, boolean enabled, int maxPlayers, int onlinePlayers) {
    this.bmbfServerName = bmbfServerName;
    this.enabled = enabled;
    this.maxPlayers = maxPlayers;
    this.onlinePlayers = onlinePlayers;
  }

  public static BmbfServerData of(String bmbfServerName, boolean enabled, int maxPlayers, int onlinePlayers) {
    return new BmbfServerData(bmbfServerName, enabled, maxPlayers, onlinePlayers);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getBmbfServerName() {
    return bmbfServerName;
  }

  public int getOnlinePlayers() {
    return onlinePlayers;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }
}

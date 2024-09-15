package dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data;

public class SurvivalServerData {
  private String survivalServerName = "";
  private int maxPlayers = 0, onlinePlayers = 0;

  public SurvivalServerData() {
  }

  public SurvivalServerData(String survivalServerName, int maxPlayers, int onlinePlayers) {
    this.survivalServerName = survivalServerName;
    this.maxPlayers = maxPlayers;
    this.onlinePlayers = onlinePlayers;
  }

  public static SurvivalServerData of(String survivalServerName, int maxPlayers, int onlinePlayers) {
    return new SurvivalServerData(survivalServerName, maxPlayers, onlinePlayers);
  }

  public String getSurvivalServerName() {
    return survivalServerName;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public int getOnlinePlayers() {
    return onlinePlayers;
  }

}

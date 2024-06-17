package dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data;

public class CommunityServerData {
  private String communityServerName = "";
  private int maxPlayers = 0, onlinePlayers = 0;

  public CommunityServerData() {
  }

  public CommunityServerData(String communityServerName, int maxPlayers, int onlinePlayers) {
    this.communityServerName = communityServerName;
    this.maxPlayers = maxPlayers;
    this.onlinePlayers = onlinePlayers;
  }

  public static CommunityServerData of(String communityServerName, int maxPlayers, int onlinePlayers) {
    return new CommunityServerData(communityServerName, maxPlayers, onlinePlayers);
  }
}

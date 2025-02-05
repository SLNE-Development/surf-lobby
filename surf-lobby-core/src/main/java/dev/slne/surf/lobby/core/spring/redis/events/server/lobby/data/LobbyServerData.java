package dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data;

import java.util.stream.Collector;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

public class LobbyServerData implements Comparable<LobbyServerData> {
  private String lobbyServerName = "";
  private int maxPlayers = 0, onlinePlayers = 0;

  protected LobbyServerData() {
  }

  public LobbyServerData(String lobbyServerName, int maxPlayers, int onlinePlayers) {
    this.lobbyServerName = lobbyServerName;
    this.maxPlayers = maxPlayers;
    this.onlinePlayers = onlinePlayers;
  }

  public static LobbyServerData of(String lobbyServerName, int maxPlayers, int onlinePlayers) {
    return new LobbyServerData(lobbyServerName, maxPlayers, onlinePlayers);
  }

  @Override
  public int compareTo(@NotNull LobbyServerData o) {
    return lobbyServerName.compareTo(o.lobbyServerName);
  }

  public String getLobbyServerName() {
    return lobbyServerName;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public int getOnlinePlayers() {
    return onlinePlayers;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public static Collector<LobbyServerData, ?, ListOrderedMap<String, LobbyServerData>> toMap() {
    return Collector.of(
        ListOrderedMap::new,
        (map, element) -> map.put(element.getLobbyServerName(), element),
        (left, right) -> {
          left.putAll(right);
          return left;
        },
        Collector.Characteristics.IDENTITY_FINISH
    );
  }
}

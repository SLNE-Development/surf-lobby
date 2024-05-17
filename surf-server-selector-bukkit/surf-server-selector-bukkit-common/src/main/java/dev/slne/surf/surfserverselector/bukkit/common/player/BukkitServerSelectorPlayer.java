package dev.slne.surf.surfserverselector.bukkit.common.player;

import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayer;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.change.RequestChangeServerEvent;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

public final class BukkitServerSelectorPlayer extends CoreServerSelectorPlayer {

  public BukkitServerSelectorPlayer(UUID uuid) {
    super(uuid);
  }

  @Override
  public Optional<Player> getPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(uuid));
  }

  @Contract(pure = true)
  @Override
  public void changeServer(String serverName, boolean sendFeedback,
      boolean fallbackToLobbyWithLowestPlayerCount) {
    new RequestChangeServerEvent(uuid, serverName, sendFeedback, fallbackToLobbyWithLowestPlayerCount).call();
  }
}

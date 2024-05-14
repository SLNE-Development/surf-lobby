package dev.slne.surf.surfserverselector.bukkit.player;

import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayer;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class BukkitServerSelectorPlayer extends CoreServerSelectorPlayer {

  public BukkitServerSelectorPlayer(UUID uuid) {
    super(uuid);
  }

  @Override
  public Optional<Player> player() {
    return Optional.ofNullable(Bukkit.getPlayer(uuid));
  }
}

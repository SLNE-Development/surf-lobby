package dev.slne.surf.surfserverselector.velocity.player;

import com.velocitypowered.api.proxy.Player;
import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayer;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import java.util.Optional;
import java.util.UUID;

public final class VelocityServerSelectorPlayer extends CoreServerSelectorPlayer {

  public VelocityServerSelectorPlayer(UUID uuid) {
    super(uuid);
  }

  @Override
  public Optional<Player> player() {
    return VelocityMain.getInstance().getServer().getPlayer(uuid);
  }
}

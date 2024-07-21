package dev.slne.surf.lobby.core.player;

import dev.slne.surf.lobby.api.player.LobbyPlayer;
import java.util.UUID;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.Contract;

/**
 * Abstract implementation of {@link LobbyPlayer} that provides core functionality shared by all
 * concrete player classes in different server environments.
 */
public abstract class CoreLobbyPlayer implements LobbyPlayer {
  private static final String PRIORITY_LEVEL_META_DATA = "priority";

  protected final UUID uuid;

  /**
   * Constructs a new CoreLobbyPlayer with the specified UUID.
   *
   * @param uuid the UUID of the player.
   */
  @Contract(pure = true)
  public CoreLobbyPlayer(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * {@inheritDoc}
   *
   * @return {@inheritDoc}
   */
  @Override
  public UUID getUuid() {
    return this.uuid;
  }

  /**
   * {@inheritDoc}
   *
   * @return {@inheritDoc}
   */
  @Override
  public int getPriority() { // TODO: 14.05.2024 18:17 - group?
    final User user = LuckPermsProvider.get().getUserManager().getUser(uuid);

    if (user != null) {
      return user.getCachedData().getMetaData().getMetaValue(PRIORITY_LEVEL_META_DATA, Integer::parseInt).orElse(-1);
    }

    return -1;
  }
}

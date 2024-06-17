package dev.slne.surf.lobby.bukkit.server;

import dev.slne.surf.lobby.bukkit.CommonBukkitMain;
import dev.slne.surf.lobby.bukkit.server.instance.BukkitLobbyInstance;
import org.jetbrains.annotations.NotNull;

public final class BukkitMain extends CommonBukkitMain {

  public BukkitMain() {
    super(new BukkitLobbyInstance());
  }

  public static @NotNull BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }
}

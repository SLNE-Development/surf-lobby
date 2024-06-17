package dev.slne.surf.lobby.bukkit.lobby;

import dev.slne.surf.lobby.bukkit.CommonBukkitMain;
import dev.slne.surf.lobby.bukkit.lobby.instance.BukkitLobbyInstance;
import org.jetbrains.annotations.NotNull;

public final class BukkitMain extends CommonBukkitMain {

  public BukkitMain() {
    super(new BukkitLobbyInstance());
  }

  @Override
  public void onLoad() {
    super.onLoad();
  }

  @Override
  public void onEnable() {
    super.onEnable();
  }

  public static @NotNull BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }
}

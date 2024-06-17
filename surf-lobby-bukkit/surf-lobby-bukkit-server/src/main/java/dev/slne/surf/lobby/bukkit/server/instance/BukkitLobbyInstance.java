package dev.slne.surf.lobby.bukkit.server.instance;

import dev.slne.surf.lobby.bukkit.common.instance.BukkitCommonLobbyInstance;
import dev.slne.surf.lobby.bukkit.server.BukkitMain;
import dev.slne.surf.lobby.bukkit.server.command.CommandManager;

public final class BukkitLobbyInstance extends BukkitCommonLobbyInstance {

  @Override
  public void onLoad() {
    super.onLoad();

    CommandManager.INSTANCE.registerCommands();
  }

  @Override
  public void onEnable() {
    super.onEnable();
  }

  @Override
  public void onDisable() {
    super.onDisable();

    CommandManager.INSTANCE.unregisterCommands();
  }

  @Override
  protected BukkitMain getInstance() {
    return BukkitMain.getInstance();
  }
}

package dev.slne.surf.lobby.bukkit.common.tasks.sync;

import dev.slne.surf.lobby.core.spring.redis.events.server.sync.MaxPlayerCountSync;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public final class SyncTask extends BukkitRunnable {

  @Override
  public void run() {
    sync();
  }

  public static void sync() {
    int maxPlayers = Bukkit.getMaxPlayers();
    new MaxPlayerCountSync(maxPlayers).call();
  }
}
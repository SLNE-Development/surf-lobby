package dev.slne.surf.lobby.bukkit.common.tasks.sync;

import dev.slne.surf.lobby.bukkit.CommonBukkitMain;
import dev.slne.surf.lobby.core.spring.redis.events.server.sync.MaxPlayerCountSync;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Bukkit;

public final class SyncTask implements Consumer<ScheduledTask> {

  private ScheduledTask task;
  private final int SYNC_DELAY = 1;

  public void start() {
    this.task = Bukkit.getAsyncScheduler().runAtFixedRate(CommonBukkitMain.getProvidingPlugin(), this, 0, SYNC_DELAY, TimeUnit.SECONDS);
  }

  public void cancel() {
    if (task != null) {
      task.cancel();
    }
  }

  public void accept(ScheduledTask scheduledTask) {
    sync();
  }

  public static void sync() {
    int maxPlayers = Bukkit.getMaxPlayers();
    new MaxPlayerCountSync(maxPlayers).call();
  }
}
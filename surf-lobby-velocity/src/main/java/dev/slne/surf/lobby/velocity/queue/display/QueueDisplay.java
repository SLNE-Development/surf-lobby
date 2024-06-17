package dev.slne.surf.lobby.velocity.queue.display;

import static com.google.common.base.Preconditions.checkState;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.surfapi.core.api.messages.Colors;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class QueueDisplay implements Runnable {

  public static final QueueDisplay INSTANCE = new QueueDisplay();
  private static final char[] ANIMATION_CHARS = new char[]{
      '◴', '◷', '◶', '◵'
//      '▁', // ▁ Lower one eighth block
//      '▂', // ▂ Lower one quarter block
//      '▃', // ▃ Lower three eighths block
//      '▄', // ▄ Lower half block
//      '▅', // ▅ Lower five eighths block
//      '▆', // ▆ Lower three quarters block
//      '▇', // ▇ Lower seven eighths block*
//      '█', // █ Full block
//      '▇', // ▇ Lower seven eighths block
//      '▆', // ▆ Lower three quarters block
//      '▅', // ▅ Lower five eighths block
//      '▄', // ▄ Lower half block
//      '▃', // ▃ Lower three eighths block
//      '▂', // ▂ Lower one quarter block
//      '▁'  // ▁ Lower one eighth block
//      '⠁', '⠃', '⠇', '⠧', '⠷', '⠿', '⡿', '⢿',
//      '⣟', '⣯', '⣷', '⣾', '⣿'
//      , '⣾', '⣷', '⣯', '⣟', '⢿', '⡿', '⠿', '⠷', '⠧', '⠇', '⠃'
  };

  private @Nullable ScheduledTask task;
  private int animationIndex = 0;

  public void setup(@NotNull ProxyServer proxyServer, Object plugin) {
    checkState(task == null, "Task is already setup");

    task = proxyServer.getScheduler().buildTask(plugin, this)
        .repeat(1, TimeUnit.SECONDS)
        .schedule();
  }

  public void destroy() {
    checkState(task != null, "Task is not setup");

    task.cancel();
    task = null;
  }

  @Override
  public void run() {

    LobbyApi.getInstance().getQueueRegistry().getPlayerQueues()
        .forEach((player, queue) -> {
          // char Warteschlange für serverName | Position 64 von 100
          player.sendActionBar(Component.text()
              .append(Component.text(ANIMATION_CHARS[animationIndex], Colors.WHITE, TextDecoration.BOLD))
              .append(Component.text(" Warteschlange für ", Colors.INFO))
              .append(Component.text(queue.getServerName(), Colors.VARIABLE_VALUE))
              .append(Component.text(" | ", Colors.SPACER))
              .append(Component.text("Position ", Colors.INFO))
              .append(Component.text(queue.getQueuePosition(player) + 1, Colors.VARIABLE_VALUE))
              .append(Component.text(" von ", Colors.INFO))
              .append(Component.text(queue.getPlayersInQueue().size(), Colors.VARIABLE_VALUE))
              .append(Component.text(" "))
              .append(Component.text(ANIMATION_CHARS[animationIndex], Colors.WHITE, TextDecoration.BOLD))
              .build());
        });

    if (animationIndex++ == ANIMATION_CHARS.length - 1) {
      animationIndex = 0;
    }
  }
}

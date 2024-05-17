package dev.slne.surf.surfserverselector.bukkit.lobby;

import dev.slne.surf.surfserverselector.bukkit.CommonBukkitMain;
import dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.switcher.LobbySwitcherView;
import dev.slne.surf.surfserverselector.bukkit.lobby.instance.BukkitSurfServerSelectorInstance;
import me.devnatan.inventoryframework.ViewFrame;
import org.jetbrains.annotations.NotNull;

public final class BukkitMain extends CommonBukkitMain {

  private ViewFrame viewFrame;

  public BukkitMain() {
    super(new BukkitSurfServerSelectorInstance());
  }

  @Override
  public void onLoad() {
    super.onLoad();

    viewFrame = ViewFrame.create(this)
        .enableDebug()
        .with(new LobbySwitcherView())
        .register();
  }

  public ViewFrame getViewFrame() {
    return viewFrame;
  }

  public static @NotNull BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }
}

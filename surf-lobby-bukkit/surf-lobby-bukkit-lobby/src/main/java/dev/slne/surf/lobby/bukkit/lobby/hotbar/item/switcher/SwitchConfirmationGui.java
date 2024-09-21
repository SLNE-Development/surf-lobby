package dev.slne.surf.lobby.bukkit.lobby.hotbar.item.switcher;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.slne.surf.lobby.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import java.util.List;
import java.util.function.Consumer;
import kotlin.collections.ArrayDeque;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Emitter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SwitchConfirmationGui extends ChestGui {

  private final ItemStack FILLER =
    ItemStackBuilder.create(Material.BLACK_STAINED_GLASS_PANE).withDisplayName(Component.text(" ")).build();

  private BukkitTask task;

  public SwitchConfirmationGui(List<Component> question, Consumer<InventoryClickEvent> confirmation) {
    super(5, ComponentHolder.of(Component.text("Survival Bestätigung")));

    setOnGlobalClick(event -> event.setCancelled(true));
    setOnGlobalDrag(event -> event.setCancelled(true));

    setOnClose(event -> {
      try {
        if (task != null && !task.isCancelled()) {
          task.cancel();
        }
      } catch(Exception ignored) {

      }
    });

    StaticPane staticPane = new StaticPane(0, 0, 9, 5);

    OutlinePane top = new OutlinePane(0, 0, 9, 1);
    top.setRepeat(true);
    top.addItem(new GuiItem(FILLER));

    OutlinePane bottom = new OutlinePane(0, 4, 9, 1);
    bottom.setRepeat(true);
    bottom.addItem(new GuiItem(FILLER));

    List<Component> lore = new ArrayDeque<>(question);

    ItemStack questionItem = ItemStackBuilder.create(Material.BARRIER).withDisplayName(Component.text("Bestätigung erforderlich",
        NamedTextColor.RED, TextDecoration.BOLD)).withLore(lore).build();
    ItemStack questionItem2 = ItemStackBuilder.create(Material.LIME_DYE).withDisplayName(Component.text("Bestätigung erforderlich",
        NamedTextColor.GREEN, TextDecoration.BOLD)).withLore(lore).build();

    staticPane.addItem(new GuiItem(questionItem, event -> {
      event.getWhoClicked().playSound(Sound.sound().type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS).build(),
          Emitter.self());

      event.getWhoClicked().sendMessage(Component.text("Bitte lies dir die Frage genau durch und bestätige sie mit einem Klick auf das Item. Dies ist aus Sicherheitsgründen erst nach 15 Sekunden möglich.",
          NamedTextColor.RED));
    }), 4, 2);

    task = new BukkitRunnable() {
      @Override
      public void run() {

        staticPane.removeItem(4, 2);

        staticPane.addItem(new GuiItem(questionItem2, event -> {
          confirmation.accept(event);
          event.getWhoClicked().closeInventory();
        }), 4, 2);

        update();
      }
    }.runTaskLater(BukkitMain.getInstance(), 15*20L);

    addPane(staticPane);
    addPane(top);
    addPane(bottom);
   }
}

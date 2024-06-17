package dev.slne.surf.lobby.bukkit.lobby.jnr.game.block;

import dev.slne.surf.lobby.bukkit.lobby.jnr.data.JNRDataManager;
import dev.slne.surf.lobby.bukkit.lobby.jnr.data.JNRPlayerDataManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class JNRBlockManager {
  public static final JNRBlockManager INSTANCE = new JNRBlockManager();

  private JNRBlockManager() {
  }

  public void placeBlock(Player player, @NotNull Location blockLocation) {
    final Material material = JNRPlayerDataManager.INSTANCE.getBlockMaterial(player);
    final Block block = blockLocation.getBlock();

    block.setType(material, false);
    JNRDataManager.INSTANCE.addPlacedBlock(block.getLocation());
  }

  public void removeBlock(@NotNull Location blockLocation) {
    final Block block = blockLocation.getBlock();

    block.setType(Material.AIR, false);
    JNRDataManager.INSTANCE.removePlacedBlock(block.getLocation());
  }
}

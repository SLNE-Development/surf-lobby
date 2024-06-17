package dev.slne.surf.lobby.bukkit.lobby.jnr.data;

import com.jeff_media.morepersistentdatatypes.DataType;
import dev.slne.surf.lobby.bukkit.lobby.jnr.data.type.Data;
import dev.slne.surf.surfserverselector.bukkit.lobby.jnr.random.JNRRandomAccessor;
import dev.slne.surf.surfserverselector.bukkit.lobby.jnr.registry.JNRBlockRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class JNRPlayerDataManager extends JNRCommonManager {
  public static final JNRPlayerDataManager INSTANCE = new JNRPlayerDataManager();

  // @formatter:off
  private static final Data<String, Material> JNR_PLAYER_BLOCK_MATERIAL = Data.of("block_material", DataType.asEnum(Material.class), () -> JNRRandomAccessor.getRandomElement(JNRBlockRegistry.BLOCKS));
  private static final Data<Byte, Boolean> JNR_PLAYER_IN_GAME = Data.of("in_game", DataType.BOOLEAN, () -> false);
//  private static final Data<Byte>
  // @formatter:on

  private JNRPlayerDataManager() {
    super();
  }

  public Material getBlockMaterial(Player player) {
//    DataType.BLOCK_DATA*
    return getData(player, JNR_PLAYER_BLOCK_MATERIAL);
  }

  public boolean isInGame(Player player) {
    return getData(player, JNR_PLAYER_IN_GAME);
  }
}

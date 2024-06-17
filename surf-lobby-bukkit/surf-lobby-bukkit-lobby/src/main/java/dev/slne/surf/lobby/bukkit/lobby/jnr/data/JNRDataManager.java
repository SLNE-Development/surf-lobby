package dev.slne.surf.lobby.bukkit.lobby.jnr.data;

import com.jeff_media.morepersistentdatatypes.DataType;
import dev.slne.surf.lobby.bukkit.lobby.jnr.data.type.Data;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JNRDataManager extends JNRCommonManager {

  public static final JNRDataManager INSTANCE = new JNRDataManager();

  // @formatter:off
  private static final Data<byte[], Player[]> JNR_PLAYERS = Data.of("jnr_players", DataType.PLAYER_ARRAY, () -> new Player[0]);
  private static final Data<byte[], Location[]> JNR_PLACED_BLOCKS = Data.of("jnr_placed_blocks", DataType.LOCATION_ARRAY, () -> new Location[0]);
  private static final Data<byte[], @Nullable Location> JNR_START_LOCATION = Data.of("jnr_start_location", DataType.LOCATION, () -> null);
  // @formatter:on

  private final Supplier<World> world = () -> Bukkit.getWorlds().getFirst();

  private JNRDataManager() {
  }


  public void addRunningPlayer(@NotNull Player player) {
    editData(JNR_PLAYERS, players -> ArrayUtils.add(players, player));
  }

  public void removeRunningPlayer(@NotNull Player player) {
    editData(JNR_PLAYERS, players -> ArrayUtils.removeElement(players, player));
  }

  public boolean isRunningPlayer(@NotNull Player player) {
    return ArrayUtils.contains(getData(JNR_PLAYERS), player);
  }

  public void addPlacedBlock(Location blockLocation) {
    editData(JNR_PLACED_BLOCKS, blocks -> ArrayUtils.add(blocks, blockLocation));
  }

  public void removePlacedBlock(Location blockLocation) {
    editData(JNR_PLACED_BLOCKS, blocks -> ArrayUtils.removeElement(blocks, blockLocation));
  }

  public Optional<Location> getStartLocation() {
    return Optional.ofNullable(getData(JNR_START_LOCATION));
  }

  public void setStartLocation(@Nullable Location location) {
    editData(JNR_START_LOCATION, oldLocation -> location);
  }

  private <P, C> void editData(@NotNull Data<P, C> dataType, UnaryOperator<C> edit) {
    editData(world.get().getPersistentDataContainer(), dataType, edit);
  }

  private <P, C>  C getData(@NotNull Data<P, C> dataType) {
    return getData(world.get().getPersistentDataContainer(), dataType);
  }
}

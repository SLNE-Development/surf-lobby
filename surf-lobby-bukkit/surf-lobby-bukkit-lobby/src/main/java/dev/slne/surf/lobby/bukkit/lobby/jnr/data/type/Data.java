package dev.slne.surf.lobby.bukkit.lobby.jnr.data.type;

import static dev.slne.surf.surfapi.bukkit.api.util.UtilBukkit.key;

import java.util.function.Supplier;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

public final class Data<P, C> {

  private final NamespacedKey key;
  private final PersistentDataType<P, C> type;
  private final Supplier<C> createDefault;

  public Data(String key, PersistentDataType<P, C> type, Supplier<C> createDefault) {
    this.key = key(key);
    this.type = type;
    this.createDefault = createDefault;
  }

  public C createDefault() {
    return createDefault.get();
  }

  public NamespacedKey getKey() {
    return key;
  }

  public PersistentDataType<P, C> getType() {
    return type;
  }

  public static <P, C> Data<P, C> of(String key, PersistentDataType<P, C> type, Supplier<C> createDefault) {
    return new Data<>(key, type, createDefault);
  }
}

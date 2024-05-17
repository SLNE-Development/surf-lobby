package dev.slne.surf.surfserverselector.bukkit.common.util.pdc;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class KeyDataType implements PersistentDataType<String, Key> {

  static final KeyDataType INSTANCE = new KeyDataType();

  private KeyDataType() {
  }

  @Contract(pure = true)
  @Override
  public @NotNull Class<String> getPrimitiveType() {
    return String.class;
  }

  @Contract(pure = true)
  @Override
  public @NotNull Class<Key> getComplexType() {
    return Key.class;
  }

  @Override
  public @NotNull String toPrimitive(@NotNull Key complex,
      @NotNull PersistentDataAdapterContext context) {
    return complex.asString();
  }

  @Override
  public @NotNull Key fromPrimitive(@KeyPattern @NotNull String primitive,
      @NotNull PersistentDataAdapterContext context) {
    return Key.key(primitive);
  }
}

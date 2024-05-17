package dev.slne.surf.surfserverselector.core.util;

import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ListUtil {

  @Contract(pure = true)
  public static <T> @Nullable T getOrNull(@NotNull List<T> list, int index) {
    try {
      return list.get(index);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }
}

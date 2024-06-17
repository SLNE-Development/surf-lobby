package dev.slne.surf.lobby.bukkit.lobby.jnr.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public final class JNRRandomAccessor {

  private static final ComponentLogger LOGGER = ComponentLogger.logger("JNRRandomAccessor");
  private static final SecureRandom RANDOM;

  static {
    SecureRandom tempRandom;
    try {
      tempRandom = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      tempRandom = new SecureRandom();
      LOGGER.error("Failed to get strong SecureRandom instance, falling back to default SecureRandom", e);
    }

    RANDOM = tempRandom;
  }

  public static SecureRandom getRandom() {
    return RANDOM;
  }

  public static <T> T getRandomElement(T[] array) {
    return array[RANDOM.nextInt(array.length)];
  }

  public static <T> T getRandomElement(List<T> list) {
    return list.get(RANDOM.nextInt(list.size()));
  }
}

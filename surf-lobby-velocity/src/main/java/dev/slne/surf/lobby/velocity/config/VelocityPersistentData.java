package dev.slne.surf.lobby.velocity.config;

import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.spring.redis.listener.lobby.SettingsManager;
import java.nio.file.Path;
import org.jetbrains.annotations.Contract;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@ConfigSerializable
public class VelocityPersistentData {

  private static YamlConfigurationLoader loader;
  private static CommentedConfigurationNode node;
  private static VelocityPersistentData instance;

  @Comment("Whether the event server is enabled")
  private boolean eventServerEnabled = false;

  public boolean isEventServerEnabled() {
    return eventServerEnabled;
  }

  public void setEventServerEnabled(boolean eventServerEnabled) {
    this.eventServerEnabled = eventServerEnabled;
    SettingsManager.update();
  }

  public static void load() throws ConfigurateException {
    final Path path = VelocityMain.getInstance().getDataDirectory().resolve("persistent-data.yml");
    loader = YamlConfigurationLoader.builder()
        .path(path)
        .indent(2)
        .build();

    node = loader.load();
    instance = node.get(VelocityPersistentData.class);

    loader.save(node);
    node.set(VelocityPersistentData.class, instance);
  }

  @Contract(pure = true)
  public static VelocityPersistentData get() {
    return instance;
  }

  public static void save() throws ConfigurateException {
    node.set(VelocityPersistentData.class, get());
    loader.save(node);
  }
}

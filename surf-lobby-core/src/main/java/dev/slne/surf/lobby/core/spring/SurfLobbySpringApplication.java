package dev.slne.surf.lobby.core.spring;

import dev.slne.data.api.DataApi;
import dev.slne.data.api.spring.SurfSpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SurfSpringApplication(scanBasePackages = "dev.slne.surf.lobby", scanFeignBasePackages = "dev.slne.surf.lobby.core.spring.feign")
public class SurfLobbySpringApplication {

  public static ConfigurableApplicationContext run(ClassLoader classLoader) {
    return DataApi.run(SurfLobbySpringApplication.class, classLoader);
  }
}
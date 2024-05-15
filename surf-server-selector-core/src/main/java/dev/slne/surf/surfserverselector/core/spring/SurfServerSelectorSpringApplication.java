package dev.slne.surf.surfserverselector.core.spring;

import dev.slne.data.api.DataApi;
import dev.slne.data.api.spring.SurfSpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SurfSpringApplication(scanBasePackages = "dev.slne.surf.surfserverselector", scanFeignBasePackages = "dev.slne.surf.surfserverselector.core.spring.feign")
public class SurfServerSelectorSpringApplication {

  public static ConfigurableApplicationContext run(ClassLoader classLoader) {
    return DataApi.run(SurfServerSelectorSpringApplication.class, classLoader);
  }
}
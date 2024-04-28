package com.epam.gymapp.mainmicroservice.actuator;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CPUHealthIndicator implements HealthIndicator {

  @Override
  public Health health() {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    double cpuLoad = osBean.getCpuLoad();
    double threshold = 0.8;
    return cpuLoad <= threshold
        ? Health.up().withDetail("load", cpuLoad).withDetail("threshold", threshold).build()
        : Health.down().withDetail("load", cpuLoad).withDetail("threshold", threshold).build();
  }
}

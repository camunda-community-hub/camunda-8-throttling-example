package org.example.camunda.process.solution.facade;

import io.camunda.zeebe.client.ZeebeClient;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.example.camunda.process.solution.config.ThrottlingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MainController {

  private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

  private final ZeebeClient zeebe;
  private final ThrottlingConfig throttlingConfig;
  private boolean started = false;
  private long nbInstancesCreated = 0;
  private long nbInstancesPerDeciSec = 0;

  public MainController(ZeebeClient client, ThrottlingConfig throttlingConfig) {
    this.zeebe = client;
    this.throttlingConfig = throttlingConfig;
  }

  @PostMapping("/start")
  public void startProcessInstance() {
    nbInstancesCreated = 0;
    started = true;
    nbInstancesPerDeciSec = throttlingConfig.getNbInstancesToCreatePerSecond() / 10;
  }

  @PostMapping("/stop")
  public void stopProcessInstance() {
    started = false;
  }

  @Scheduled(fixedRate = 100, initialDelay = 1000)
  public void startProcessInstances() {
    if (started && nbInstancesCreated < throttlingConfig.getNbInstancesToCreate()) {
      for (int i = 0; i < nbInstancesPerDeciSec; i++) {
        zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId(throttlingConfig.getBpmnProcessId())
            .latestVersion()
            .variables(Map.of("numInstance", nbInstancesCreated++))
            .send();
      }
    } else {
      started = false;
    }
  }

  @PostMapping("/config")
  public ThrottlingConfig updateConfig(@RequestBody ThrottlingConfig throttlingConfig)
      throws IllegalAccessException, InvocationTargetException {
    BeanUtils.copyProperties(this.throttlingConfig, throttlingConfig);
    return getConfig();
  }

  @GetMapping("/config")
  public ThrottlingConfig getConfig() throws IllegalAccessException, InvocationTargetException {
    ThrottlingConfig clone = new ThrottlingConfig();
    BeanUtils.copyProperties(clone, this.throttlingConfig);
    return clone;
  }
}

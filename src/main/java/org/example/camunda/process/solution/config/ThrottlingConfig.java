package org.example.camunda.process.solution.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "throttling")
public class ThrottlingConfig {

  private String bpmnProcessId = "throttling-process";

  private Long nbInstancesToCreate = 1000L;

  private Long nbInstancesToCreatePerSecond = 10L;

  private Long throttlingLimit = 20L;

  private Long slowTaskDuration = 12000L;

  public String getBpmnProcessId() {
    return bpmnProcessId;
  }

  public void setBpmnProcessId(String bpmnProcessId) {
    this.bpmnProcessId = bpmnProcessId;
  }

  public Long getNbInstancesToCreate() {
    return nbInstancesToCreate;
  }

  public void setNbInstancesToCreate(Long nbInstancesToCreate) {
    this.nbInstancesToCreate = nbInstancesToCreate;
  }

  public Long getNbInstancesToCreatePerSecond() {
    return nbInstancesToCreatePerSecond;
  }

  public void setNbInstancesToCreatePerSecond(Long nbInstancesToCreatePerSecond) {
    this.nbInstancesToCreatePerSecond = nbInstancesToCreatePerSecond;
  }

  public Long getThrottlingLimit() {
    return throttlingLimit;
  }

  public void setThrottlingLimit(Long throttlingLimit) {
    this.throttlingLimit = throttlingLimit;
  }

  public Long getSlowTaskDuration() {
    return slowTaskDuration;
  }

  public void setSlowTaskDuration(Long slowTaskDuration) {
    this.slowTaskDuration = slowTaskDuration;
  }
}

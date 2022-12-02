package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.example.camunda.process.solution.service.ThrottlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ThrottlingWorkers {

  private static final Logger LOG = LoggerFactory.getLogger(ThrottlingWorkers.class);

  private ThrottlingService throttlingService;

  public ThrottlingWorkers(ThrottlingService throttlingService) {
    this.throttlingService = throttlingService;
  }

  @JobWorker(type = "checkLimit", autoComplete = false)
  public void checkLimit(ActivatedJob job) {
    if (!throttlingService.executeOrThrottle(job.getKey())) {
      LOG.info(
          "QUEUING job "
              + job.getKey()
              + " for instance number "
              + job.getVariablesAsMap().get("numInstance"));
    }
  }

  @JobWorker(type = "completeInstance")
  public void completeInstance(ActivatedJob job) {
    Long nextKey = throttlingService.completeAndUnqueue();
    if (nextKey != null) {
      LOG.info("UNQUEUING job " + nextKey);
    }
  }
}

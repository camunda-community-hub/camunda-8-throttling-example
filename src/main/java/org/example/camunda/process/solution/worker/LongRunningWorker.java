package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import java.util.Map;
import org.example.camunda.process.solution.config.ThrottlingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LongRunningWorker {

  private static final Logger LOG = LoggerFactory.getLogger(LongRunningWorker.class);

  @Autowired private ThrottlingConfig throttlingConfig;
  @Autowired private ZeebeClient zeebeClient;

  @JobWorker(type = "longRunning", autoComplete = false)
  public void slowExecution(ActivatedJob job) throws InterruptedException {
    Map<String, Object> vars = job.getVariablesAsMap();
    LOG.info("EXECUTING instance number " + vars.get("numInstance"));
    DelayedTaskThread delayedCompletion = new DelayedTaskThread(job.getKey());
    delayedCompletion.start();
  }

  class DelayedTaskThread extends Thread {

    Long jobKey;

    public DelayedTaskThread(Long jobKey) {
      this.jobKey = jobKey;
    }

    public void run() {
      try {
        Thread.sleep(throttlingConfig.getSlowTaskDuration());
      } catch (InterruptedException e) {
        LOG.error("error with thread sleep", e);
      }
      zeebeClient.newCompleteCommand(jobKey).send();
    }
  }
}

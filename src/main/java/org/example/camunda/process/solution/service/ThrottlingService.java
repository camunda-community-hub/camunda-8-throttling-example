package org.example.camunda.process.solution.service;

import io.camunda.zeebe.client.ZeebeClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.example.camunda.process.solution.config.ThrottlingConfig;
import org.example.camunda.process.solution.dao.PendingJobKeyRepository;
import org.example.camunda.process.solution.model.PendingJobKey;
import org.springframework.stereotype.Service;

/**
 * Ideally, the pendingJobs should be managed in a cache that allows atomic writing. For this
 * example, customer actively required a "db" management. So it uses a local list for reading.
 * Writings are done in the list and in DB. If the worker goes down, the states can be retrieved
 * from the DB. The count will not be accurate anymore, leading to higher throughpout at the
 * beginning. This could also be managed if required.
 */
@Transactional
@Service
public class ThrottlingService {

  private List<Long> pendingJobKeys = new ArrayList<>();
  private AtomicLong runningJobs = new AtomicLong();

  private final ThrottlingConfig throttlingConfig;
  private final ZeebeClient zeebeClient;
  private final PendingJobKeyRepository pendingJobKeyRepository;

  public ThrottlingService(
      ZeebeClient client,
      ThrottlingConfig throttlingConfig,
      PendingJobKeyRepository pendingJobKeyRepository) {
    this.zeebeClient = client;
    this.throttlingConfig = throttlingConfig;
    this.pendingJobKeyRepository = pendingJobKeyRepository;
  }

  /**
   * @param jobKey
   * @return true if job is completed and false if throttled
   */
  public boolean executeOrThrottle(Long jobKey) {
    if (runningJobs.get() >= throttlingConfig.getThrottlingLimit()) {
      // we are over limit, we can't continue
      pendingJobKeys.add(jobKey);
      pendingJobKeyRepository.save(new PendingJobKey(jobKey));
      return false;
    }
    // we are under limit, we increse the count and continue
    runningJobs.getAndIncrement();
    zeebeClient.newCompleteCommand(jobKey).send();
    return true;
  }

  /**
   * Call at the end of process instance to unqueue the next jobKey.
   *
   * @return the jobKey that was unqueued if any.
   */
  public synchronized Long completeAndUnqueue() {
    if (!pendingJobKeys.isEmpty()) {
      // pending job is executed. Number of running job remains unchanged
      Long nextKey = pendingJobKeys.get(0);
      zeebeClient.newCompleteCommand(nextKey).send();
      pendingJobKeys.remove(0);
      pendingJobKeyRepository.deleteByJobKey(nextKey);
      return nextKey;
    }
    // no more pending job, we decrement the number of running job
    long value = runningJobs.get();
    if (value > 0) {
      runningJobs.getAndDecrement();
    }
    return null;
  }

  @PostConstruct
  public void reloadJobsFromDb() {
    List<PendingJobKey> pendings = pendingJobKeyRepository.findAllByOrderByJobKeyAsc();
    for (PendingJobKey pending : pendings) {
      pendingJobKeys.add(pending.getJobKey());
    }
  }
}

package org.example.camunda.process.solution.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PendingJobKey {
  @Id private Long jobKey;

  public PendingJobKey() {
    super();
  }

  public PendingJobKey(Long jobKey) {
    super();
    this.jobKey = jobKey;
  }

  public Long getJobKey() {
    return jobKey;
  }

  public void setJobKey(Long jobKey) {
    this.jobKey = jobKey;
  }
}

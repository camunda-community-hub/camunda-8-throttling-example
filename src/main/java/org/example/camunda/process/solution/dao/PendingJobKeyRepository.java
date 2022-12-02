package org.example.camunda.process.solution.dao;

import java.util.List;
import org.example.camunda.process.solution.model.PendingJobKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingJobKeyRepository extends JpaRepository<PendingJobKey, Long> {

  List<PendingJobKey> findAllByOrderByJobKeyAsc();

  Long deleteByJobKey(Long jobKey);
}

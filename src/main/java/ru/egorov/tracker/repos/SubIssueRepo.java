package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.issue.SubIssue;

import java.util.Set;

public interface SubIssueRepo extends JpaRepository<SubIssue, Long> {
    Set<SubIssue> findByExecutorId(Long executorId);

    Set<SubIssue> findByCreatorId(Long creatorid);
}

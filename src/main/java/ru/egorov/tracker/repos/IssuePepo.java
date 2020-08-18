package ru.egorov.tracker.repos;

import org.springframework.data.repository.CrudRepository;
import ru.egorov.tracker.domain.issue.Issue;

public interface IssuePepo extends CrudRepository<Issue, Long> {
}

package ru.egorov.tracker.domain.storage;

import ru.egorov.tracker.domain.issue.Issue;

public interface ImplStorage {

    void addIssue(Issue issue);

    void updateIssue(Issue issue);

    void removeIssue(Issue issue);
}

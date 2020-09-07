package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.issue.Issue;

import java.util.Optional;

public interface IssueRepo extends JpaRepository<Issue, Long> {

    @Query(value = "SELECT u FROM issue u where u.creator=?", nativeQuery = true)
    Iterable<Issue> findAllByIdCreator(Long id);

    /*Iterable<Issue> findAllByProjectId(Long id);*/

    Optional<Issue> findById(Long Id);

    void delete(Issue issue);

    void deleteById(Long id);
}

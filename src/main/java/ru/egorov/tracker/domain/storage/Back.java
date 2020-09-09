package ru.egorov.tracker.domain.storage;

import ru.egorov.tracker.domain.issue.Issue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name = "backlog")
public class Back implements ImplStorage, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //private Project project;
    //@OneToMany(mappedBy = "back_log", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)

    /*@JoinTable(
            name="netto",
            joinColumns = @JoinColumn( name="back_log_id"),
            inverseJoinColumns = @JoinColumn( name="issue_id")
    )*/
    //@ElementCollection(targetClass = Issue.class)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "back_id", referencedColumnName = "id", nullable = false)
    @OrderColumn(name = "pos", nullable = false)
    private Set<Issue> issues = new HashSet<>();

    /*@Autowired
    private IssueRepo issueRepo;*/

    public Back() {
    }

    public Back(Set<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    @Override
    public void updateIssue(Issue issue) {

    }

    @Override
    public void removeIssue(Issue issue) {

    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> store) {
        this.issues = store;
    }
}

package ru.egorov.tracker.domain.storage;

import org.springframework.beans.factory.annotation.Autowired;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.repos.IssueRepo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "back_log")
public class BackLog implements ImplStorage, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //private Project project;
    //@OneToMany(mappedBy = "back_log", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name="netto",
            joinColumns = @JoinColumn( name="back_log_id"),
            inverseJoinColumns = @JoinColumn( name="issue_id")
    )

    //@ElementCollection(targetClass = Issue.class)
    //@JoinColumn(name = "back_log"/*, referencedColumnName = "id"*/)
    private Set<Issue> store = new HashSet<>();

    /*@Autowired
    private IssueRepo issueRepo;*/


    @Override
    public void addIssue(Issue issue) {
        store.add(issue);
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

    public Set<Issue> getStore() {
        return store;
    }

    public void setStore(Set<Issue> store) {
        this.store = store;
    }
}

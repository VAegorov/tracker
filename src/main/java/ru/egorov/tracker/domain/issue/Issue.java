package ru.egorov.tracker.domain.issue;

import ru.egorov.tracker.domain.User;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issue")
public class Issue /*extends IssueAbstr*/{
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*@ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "back_log", referencedColumnName = "id")
        private BackLog backLog;*/
    /*@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public Set<SubIssue> subIssues = new HashSet<>();*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Issue() {
    }

    /*public Issue(String name, String description, User creator, User executor, *//*Project project,*//*
                 IssuePriority issuePriority, IssueStatus issueStatus) {
        //super(name, creator, executor, issuePriority, issueStatus, description);
        //this.project = project;
    }*/

   /* public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }*/

    /*public Set<SubIssue> getSubIssues() {
        return subIssues;
    }

    public void setSubIssues(Set<SubIssue> subIssues) {
        this.subIssues = subIssues;
    }*/

    /*public BackLog getBackLog() {
        return backLog;
    }

    public void setBackLog(BackLog backLog) {
        this.backLog = backLog;
    }*/

}

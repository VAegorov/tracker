package ru.egorov.tracker.domain.issue;

import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issue")
public class Issue extends IssueAbstr{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project", referencedColumnName = "id")
    private Project project;

    private IssueType issueType;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SubIssue> subIssues = new HashSet<>();
    private boolean isBackLog = true;


    public Issue() {
    }

    public Issue(String name, String description, User creator, User executor, Project project,
                 IssuePriority issuePriority, IssueStatus issueStatus, IssueType issueType) {
        super(name, creator, executor, issuePriority, issueStatus, description);
        this.project = project;
        this.issueType = issueType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<SubIssue> getSubIssues() {
        return subIssues;
    }

    public void setSubIssues(Set<SubIssue> subIssues) {
        this.subIssues = subIssues;
    }

    public boolean isIsBackLog() {
        return isBackLog;
    }

    public void setIsBackLog(boolean backLog) {
        this.isBackLog = backLog;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public boolean isBackLog() {
        return isBackLog;
    }

    public void setBackLog(boolean backLog) {
        isBackLog = backLog;
    }
}

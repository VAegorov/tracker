package ru.egorov.tracker.domain.issue;

import ru.egorov.tracker.domain.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SubIssue extends IssueAbstr{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issue", referencedColumnName = "id")
    Issue issue;//parent

    public SubIssue() {
    }

    public SubIssue(String name, String description, User creator,
                    IssuePriority issuePriority, IssueStatus issueStatus, Issue issue) {
        super(name, creator, issuePriority, issueStatus, description);
        this.issue = issue;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issueParent) {
        this.issue = issueParent;
    }
}

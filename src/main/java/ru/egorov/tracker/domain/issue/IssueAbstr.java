package ru.egorov.tracker.domain.issue;

import org.springframework.stereotype.Component;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public abstract class IssueAbstr implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    private String description;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.DATE)
    private Date closeDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executor", referencedColumnName = "id")
    private User executor;

    @Enumerated(EnumType.STRING)
    private IssuePriority issuePriority;

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;

    public IssueAbstr() {
    }

    public IssueAbstr(String name, User creator, User executor, IssuePriority issuePriority,
                      IssueStatus issueStatus, String description) {
        this.name = name;
        this.creator = creator;
        this.executor = executor;
        this.date = new Date();
        this.issuePriority = issuePriority;
        this.issueStatus = issueStatus;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public IssuePriority getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(IssuePriority issuePriority) {
        this.issuePriority = issuePriority;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IssueAbstr)) return false;
        IssueAbstr that = (IssueAbstr) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

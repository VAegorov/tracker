package ru.egorov.tracker.domain.issue;

import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Issue extends IssueAbstr{

    //private String name;
    private String description;

    public Issue() {
    }

    public Issue(String name, String description, User creator, Project project,
                 IssuePriority issuePriority, IssueStatus issueStatus) {
        super(name, creator, project, issuePriority, issueStatus);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

}

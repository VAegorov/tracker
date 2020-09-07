package ru.egorov.tracker.domain;

import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.domain.storage.BackLog;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "project")
//@Embeddable
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "projectname")
    private String projectName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin", referencedColumnName = "id")
    private User admin;

    @ManyToMany
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<User> users = new HashSet<>();

    /*@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Issue> issues = new HashSet<>();*/
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BackLog backLog = new BackLog();

    public Project() {
    }

    public Project(String projectName, User user) {
        this.owner = user;
        this.admin = user;
        System.out.println("Created new project!");
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOwnerName() {
        return owner != null ? owner.getUsername() : "<none>";
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    /*public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }*/

    public boolean isOwner(User user) {
        return this.getOwner().equals(user);
    }

    public boolean isAdmin(User user) {
        return this.getAdmin().equals(user);
    }

    public HashSet<User> allUsers() {
        HashSet<User> users = new HashSet<>();
        users.add(owner);
        users.add(admin);
        users.addAll(this.users);

        return users;
    }

    public BackLog getBackLog() {
        return backLog;
    }

    public void setBackLog(BackLog backLog) {
        this.backLog = backLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

package ru.egorov.tracker.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    //@Column(name = "owner")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin", referencedColumnName = "id")
    //@Column(name = "admin")
    private User admin;

    @ManyToMany
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<User> users = new HashSet<>();


    public Project() {
    }

    public Project(String projectName, User user) {
        this.owner = user;
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
}

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
    private String projectName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_id")
    //@CollectionTable(name = "usr", joinColumns = @JoinColumn(name = "usr_id"))
    private User owner;

    //@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "project_user_role",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<User> users = new HashSet<>();


    public Project() {
    }

    public Project(String projectName, User user) {
        //this.owner = (Usr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.owner = user;
        //this.users.add(user);
        System.out.println(555);
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
}

package ru.egorov.tracker.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.egorov.tracker.domain.issue.Issue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="usr")
//@Embeddable
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Project> projectsOwner = new HashSet<>();//проекты в которых он owner

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Project> projectsAdmin = new HashSet<>();//проекты в которых он admin

    @ManyToMany
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "usr_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projectsUser = new HashSet<>();//проекты в которых он user

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Issue> issueCreator = new HashSet<>();//задачи в которых он creator

    @OneToMany(mappedBy = "executor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Issue> issueExecutor = new HashSet<>();//задачи в которых он executor

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "usr_role", joinColumns = @JoinColumn(name = "usr_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public User() {
    }

    public User(String name) {
        this.username = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Project> getProjectsOwner() {
        return projectsOwner;
    }

    public void setProjectsOwner(Set<Project> projects) {
        this.projectsOwner = projects;
    }

    public Set<Project> getProjectsUser() {
        return projectsUser;
    }

    public void setProjectsUser(Set<Project> projectsUser) {
        this.projectsUser = projectsUser;
    }

    public Set<Project> getProjectsAdmin() {
        return projectsAdmin;
    }

    public void setProjectsAdmin(Set<Project> projectsAdmin) {
        this.projectsAdmin = projectsAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

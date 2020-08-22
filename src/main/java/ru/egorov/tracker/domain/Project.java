package ru.egorov.tracker.domain;

import javax.persistence.*;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String projectname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_id")
    //@CollectionTable(name = "usr", joinColumns = @JoinColumn(name = "usr_id"))
    private User owner;

    public Project() {
    }

    public Project(String projectname, User user) {
        //this.owner = (Usr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.owner = user;
        System.out.println(555);
        this.projectname = projectname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
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
}

/*
package ru.egorov.tracker.domain;

import javax.persistence.*;

@Entity
@Table(name = "project_user")
//@IdClass(Project.class)
public class ProjectUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@AttributeOverrides({@AttributeOverride(name = "user", column = @Column(name = "user"))})

    private Project project;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_id")
    private User user;

    //@JoinColumn(name = "usr_id")
    private ProjectRole projectRole;

    public ProjectUsers() {
    }

    public ProjectUsers(Project project) {
        this.project = project;
        this.user = project.getOwner();
        this.projectRole = ProjectRole.OWNER;
    }
}
*/

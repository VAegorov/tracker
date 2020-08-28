package ru.egorov.tracker.domain.issue;

import org.springframework.stereotype.Component;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public abstract class IssueAbstr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;



    public IssueAbstr() {
    }

    public IssueAbstr(String name, User creator) {
        this.name = name;
        this.creator = creator;
        this.date = new Date();

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
}

package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    //Iterable<Project> findAllById(Long id);
    //@Query("SELECT p.id, p.projectname FROM Project p  inner join User u on u.id=p.owner.id where p.owner.id =?1")
    @Query(value = "SELECT * FROM project where usr_id=?", nativeQuery = true)
    Iterable<Project> findAllWhereById(Long id);
}

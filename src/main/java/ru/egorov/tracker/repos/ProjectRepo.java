package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.Project;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);

    //Iterable<Project> findAllById(Long id);
    //@Query("SELECT p.id, p.projectname FROM Project p where p.owner.id =?1")
    @Query(value = "SELECT * FROM project where owner=?", nativeQuery = true)
    Iterable<Project> findAllWhereByIdOwner(Long id);

    @Query(value = "SELECT * FROM project where admin=?", nativeQuery = true)
    Iterable<Project> findAllWhereByIdAdmin(Long id);

    @Query(value = "SELECT * FROM project JOIN  project_user ON project.id=project_user.project_id where project_user.usr_id=?", nativeQuery = true)
    Iterable<Project> findAllWhereByIdUser(Long id);

}

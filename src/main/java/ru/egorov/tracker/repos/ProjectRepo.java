package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.Project;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);

    Iterable<Project>findByAdminId(Long id);

    Iterable<Project>findByOwnerId(Long id);

    @Query(value = "SELECT * FROM project JOIN  project_user ON project.id=project_user.project_id where project_user.usr_id=?", nativeQuery = true)
    Iterable<Project> findAllWhereByIdUser(Long id);//проекты пользователя по его Id


}

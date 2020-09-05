package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.Project;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);

    Iterable<Project>findByAdminId(Long id);

    Iterable<Project>findByOwnerId(Long id);

    @Query(value = "SELECT * FROM project u JOIN project_user ON" +
            " u.id=project_user.project_id where project_user.usr_id=?", nativeQuery = true)
    //@Query("SELECT u FROM Project u JOIN u.users ON u.id=u.users. where User.id=?1")
    Iterable<Project> findAllWhereByIdUser(Long id);//проекты пользователя где он участник по его Id


}

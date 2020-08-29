package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egorov.tracker.domain.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findById(Long id);

    @Query(value = "SELECT usr.id, usr.active, usr.username, usr.password FROM usr WHERE usr.id NOT IN " +
            "(SELECT project_user.usr_id FROM project_user where project_user.project_id=?1 " +
            "UNION SELECT project.owner FROM project WHERE project.id=?1 " +
            "UNION SELECT project.admin FROM project WHERE project.id=18)", nativeQuery = true)
    //@Query(value = "SELECT usr.id, usr.active, usr.username, usr.password FROM usr WHERE usr.id NOT IN (SELECT project_user.usr_id FROM project_user where project_user.project_id=?)", nativeQuery = true)
    //@Query(value = "SELECT usr.id, usr.active, usr.username, usr.password FROM usr LEFT JOIN project_user ON usr.id=project_user.usr_id where project_user.usr_id IS NULL AND project_user.project_id=?", nativeQuery = true)
    Iterable<User> findNewUser(Long id);
}

package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.tracker.domain.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findById(Long id);
}

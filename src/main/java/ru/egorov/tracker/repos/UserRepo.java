package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.tracker.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

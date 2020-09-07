package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.tracker.domain.storage.BackLog;

import java.util.Optional;

public interface BackLogRepo extends JpaRepository<BackLog, Long> {

    Optional<BackLog> findById(Long Id);

}

package ru.egorov.tracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.tracker.domain.storage.Back;

import java.util.Optional;

public interface BackLogRepo extends JpaRepository<Back, Long> {

    Optional<Back> findById(Long Id);

}

package org.jetbrains.assignment.repositories;

import org.jetbrains.assignment.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {
}

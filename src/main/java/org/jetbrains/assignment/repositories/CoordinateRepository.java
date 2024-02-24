package org.jetbrains.assignment.repositories;

import org.jetbrains.assignment.entities.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
}

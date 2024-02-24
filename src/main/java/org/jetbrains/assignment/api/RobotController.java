package org.jetbrains.assignment.api;

import org.jetbrains.assignment.entities.Coordinate;
import org.jetbrains.assignment.entities.Movement;
import org.jetbrains.assignment.repositories.CoordinateRepository;
import org.jetbrains.assignment.repositories.MovementRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RobotController {

    private final CoordinateRepository coordinateRepository;
    private final MovementRepository movementRepository;

    public RobotController(CoordinateRepository coordinateRepository, MovementRepository movementRepository) {
        this.coordinateRepository = coordinateRepository;
        this.movementRepository = movementRepository;
    }

    @PostMapping("/locations")
    public List<Coordinate> locations(@RequestBody List<Movement> movements) {
        movementRepository.saveAll(movements);
        Coordinate coordinate = new Coordinate();
        coordinateRepository.save(new Coordinate(coordinate.getX(), coordinate.getY()));
        Coordinate previousCoordinate = coordinate;
        for (Movement movement : movements) {
            switch (movement.getDirection()) {
                case "EAST":
                    coordinate.setX(previousCoordinate.getX() + movement.getSteps());
                    break;
                case "WEST":
                    coordinate.setX(previousCoordinate.getX() - movement.getSteps());
                    break;
                case "NORTH":
                    coordinate.setY(previousCoordinate.getY() + movement.getSteps());
                    break;
                case "SOUTH":
                    coordinate.setY(previousCoordinate.getY() - movement.getSteps());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown direction! ("+movement.getDirection()+")");
            }
            coordinateRepository.save(new Coordinate(coordinate.getX(), coordinate.getY()));
            previousCoordinate = coordinate;
        }
        return coordinateRepository.findAll();
    }

    @PostMapping("/moves")
    public List<Movement> moves(@RequestBody List<Coordinate> coordinates) {
        coordinateRepository.saveAll(coordinates);
        List<Movement> movements = new ArrayList<>();
        for (int i = 1; i <= coordinates.size() -1; i++) {
            Movement movement = new Movement();
            String directionX = coordinates.get(i).getX() > 0 ? "EAST" : "WEST";
            String directionY = coordinates.get(i).getX() > 0 ? "NORTH" : "SOUTH";

                if (coordinates.get(i).getX() > coordinates.get(i-1).getX()) {
                    movement.setDirection(directionX);
                    movement.setSteps(coordinates.get(i).getX() + coordinates.get(i-1).getX());
                } else if (coordinates.get(i).getX() < coordinates.get(i-1).getX()) {
                    movement.setDirection(directionX);
                    movement.setSteps(Math.abs(coordinates.get(i).getX() - coordinates.get(i-1).getX()));
                } else if (coordinates.get(i).getY() > coordinates.get(i-1).getY()) {
                    movement.setDirection(directionY);
                    movement.setSteps(coordinates.get(i).getY() + coordinates.get(i-1).getY());
                } else if (coordinates.get(i).getY() < coordinates.get(i-1).getY()) {
                    movement.setDirection(directionY);
                    movement.setSteps(Math.abs(coordinates.get(i).getY() - coordinates.get(i-1).getY()));
                }
            movements.add(movement);
        }

        return movements;
    }
}
package ru.job4j.cars.repository.car;

import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.Optional;

public interface CarRepository {

    Optional<Car> create(Car car);

    boolean update(Car car);

    boolean delete(Long id);

    Optional<Car> findById(Long id);

    Collection<Car> findAll();
}

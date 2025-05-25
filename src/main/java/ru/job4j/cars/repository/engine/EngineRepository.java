package ru.job4j.cars.repository.engine;

import ru.job4j.cars.model.Engine;

import java.util.Collection;
import java.util.Optional;

public interface EngineRepository {

    Collection<Engine> findAll();

    Optional<Engine> findById(Long id);

    Optional<Engine> findByName(String name);

    boolean persist(Engine engine);

    boolean update(Engine engine);

    boolean delete(Long id);
}

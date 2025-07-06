package ru.job4j.cars.service.engine;

import ru.job4j.cars.model.Engine;

import java.util.Collection;
import java.util.Optional;

public interface EngineService {

    Collection<Engine> findAll();

    Optional<Engine> findById(Long id);

    Optional<Engine> findByName(String name);

    boolean persist(Engine engine);

    boolean update(Engine engine);

    boolean delete(Long id);
}

package ru.job4j.cars.repository.color;

import ru.job4j.cars.model.Color;

import java.util.Collection;
import java.util.Optional;

public interface ColorRepository {

    Collection<Color> findAll();

    Optional<Color> findById(Long id);

    Optional<Color> findByName(String name);

    Optional<Color> findByCode(String code);

    boolean persist(Color color);

    boolean update(Color color);

    boolean delete(Long id);
}

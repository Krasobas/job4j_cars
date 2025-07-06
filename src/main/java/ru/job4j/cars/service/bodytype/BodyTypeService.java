package ru.job4j.cars.service.bodytype;

import ru.job4j.cars.model.BodyType;

import java.util.Collection;
import java.util.Optional;

public interface BodyTypeService {

    Collection<BodyType> findAll();

    Optional<BodyType> findById(Long id);

    Optional<BodyType> findByName(String name);

    boolean persist(BodyType bodyType);

    boolean update(BodyType bodyType);

    boolean delete(Long id);
}

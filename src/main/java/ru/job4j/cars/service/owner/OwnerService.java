package ru.job4j.cars.service.owner;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OwnerService {

    Collection<Owner> findAll();

    Optional<Owner> findById(Long id);

    Optional<Owner> findByName(String name);

    boolean persist(Owner owner);

    Optional<Owner> persistIfNotExist(String name);

    List<Owner> persistIfNotExist(List<String> names);

    boolean update(Owner owner);

    boolean delete(Long id);
}

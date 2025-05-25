package ru.job4j.cars.repository.owner;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerRepository {

    Collection<Owner> findAll();

    Optional<Owner> findById(Long id);

    Optional<Owner> findByName(String name);

    boolean persist(Owner owner);

    boolean update(Owner owner);

    boolean delete(Long id);
}

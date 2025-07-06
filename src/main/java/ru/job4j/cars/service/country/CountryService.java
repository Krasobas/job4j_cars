package ru.job4j.cars.service.country;

import ru.job4j.cars.model.Country;

import java.util.Collection;
import java.util.Optional;

public interface CountryService {

    Collection<Country> findAll();

    Optional<Country> findById(Long id);

    Optional<Country> findByName(String name);

    boolean persist(Country country);

    boolean update(Country country);

    boolean delete(Long id);
}

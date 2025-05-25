package ru.job4j.cars.repository.brand;

import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Country;

import java.util.Collection;
import java.util.Optional;

public interface BrandRepository {

    Collection<Brand> findAll();

    Collection<Brand> findByCountry(Country country);

    Optional<Brand> findById(Long id);

    Optional<Brand> findByName(String name);

    boolean persist(Brand brand);

    boolean update(Brand brand);

    boolean delete(Long id);
}

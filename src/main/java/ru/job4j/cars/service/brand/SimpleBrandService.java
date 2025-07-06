package ru.job4j.cars.service.brand;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Country;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.brand.BrandRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleBrandService implements BrandService {

    private final BrandRepository repository;

    @Override
    public Collection<Brand> findAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Brand> findByCountry(Country country) {
        return repository.findByCountry(country);
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Brand> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public boolean persist(Brand brand) {
        return repository.persist(brand);
    }

    @Override
    public boolean update(Brand brand) {
        return repository.update(brand);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }
}

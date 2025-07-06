package ru.job4j.cars.service.country;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Country;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleCountryService implements CountryService {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Country> findAll() {
        try {
            return crudRepository.query(
                    "from Country order by name asc",
                    Country.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Country> findById(Long id) {
        try {
            return crudRepository.optional(
                    "from Country where id = :fId",
                    Country.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Country> findByName(String name) {
        try {
            return crudRepository.optional(
                    "from Country where name = :fName",
                    Country.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(Country country) {
        try {
            crudRepository.run(session -> session.persist(country));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(Country country) {
        try {
            return findById(country.getId())
                    .map(origin -> {
                        origin.setName(country.getName());
                        crudRepository.run(session -> session.merge(origin));
                        return origin;
                    }).isPresent();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try {
            return crudRepository.run(
                    "delete from Country where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

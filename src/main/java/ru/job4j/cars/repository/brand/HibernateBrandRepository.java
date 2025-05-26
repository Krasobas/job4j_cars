package ru.job4j.cars.repository.brand;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Country;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernateBrandRepository implements BrandRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Brand> findAll() {
        try {
            return crudRepository.query(
                    "from Brand b join fetch b.country order by b.name asc",
                    Brand.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Brand> findByCountry(Country country) {
        try {
            return crudRepository.query(
                    "from Brand b join fetch b.country where b.country = :fCountry order by b.name asc",
                    Brand.class,
                    Map.of("fCountry", country)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        try {
            return crudRepository.optional(
                    "from Brand b join fetch b.country where b.id = :fId",
                    Brand.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Brand> findByName(String name) {
        try {
            return crudRepository.optional(
                    "from Brand b join fetch b.country where b.name = :fName",
                    Brand.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(Brand brand) {
        try {
            crudRepository.run(session -> session.persist(brand));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(Brand brand) {
        try {
            return findById(brand.getId())
                    .map(origin -> {
                        origin.setName(brand.getName());
                        origin.setCountry(brand.getCountry());
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
                    "delete from Brand where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

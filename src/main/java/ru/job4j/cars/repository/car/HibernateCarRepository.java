package ru.job4j.cars.repository.car;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernateCarRepository implements CarRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Car> create(Car car) {
        try {
            crudRepository.run(session -> session.persist(car));
            return Optional.of(car);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Car car) {
        try {
            return findById(car.getId())
                    .map(origin -> {
                        car.setId(origin.getId());
                        crudRepository.run(session -> session.merge(car));
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
                    "delete from Car where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Optional<Car> findById(Long id) {
        try {
            return crudRepository.optional(
                    """
                            from Car c
                            join fetch c.brand
                            join fetch c.bodyType
                            join fetch c.engine
                            join fetch c.color
                            join fetch c.owners
                            where c.id = :fId
                            """,
                    Car.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Car> findAll() {
        try {
            return crudRepository.query(
                    """
                            from Car c
                            join fetch c.brand
                            join fetch c.bodyType
                            join fetch c.engine
                            join fetch c.color
                            join fetch c.owners
                            """,
                    Car.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }
}
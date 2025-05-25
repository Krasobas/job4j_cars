package ru.job4j.cars.repository.color;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernateColorRepository implements ColorRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Color> findAll() {
        try {
            return crudRepository.query(
                    "from Color order by name asc",
                    Color.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Color> findById(Long id) {
        try {
            return crudRepository.optional(
                    "form Color where id = :fId",
                    Color.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Color> findByName(String name) {
        try {
            return crudRepository.optional(
                    "form Color where name = :fName",
                    Color.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Color> findByCode(String code) {
        try {
            return crudRepository.optional(
                    "form Color where code = :fCode",
                    Color.class,
                    Map.of("fCode", code)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(Color color) {
        try {
            crudRepository.run(session -> session.persist(color));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(Color color) {
        try {
            return findById(color.getId())
                    .map(origin -> {
                        origin.setName(color.getName());
                        origin.setCode(color.getCode());
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
                    "delete form Color where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

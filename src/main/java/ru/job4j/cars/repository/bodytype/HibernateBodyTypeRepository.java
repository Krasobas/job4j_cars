package ru.job4j.cars.repository.bodytype;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.BodyType;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernateBodyTypeRepository implements BodyTypeRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<BodyType> findAll() {
        try {
            return crudRepository.query(
                    "from BodyType order by name asc",
                    BodyType.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<BodyType> findById(Long id) {
        try {
            return crudRepository.optional(
                    "from BodyType where id = :fId",
                    BodyType.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BodyType> findByName(String name) {
        try {
            return crudRepository.optional(
                    "from BodyType where name = :fName",
                    BodyType.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(BodyType bodyType) {
        try {
            crudRepository.run(session -> session.persist(bodyType));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(BodyType bodyType) {
        try {
            return findById(bodyType.getId())
                    .map(origin -> {
                        origin.setName(bodyType.getName());
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
                    "delete from BodyType where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

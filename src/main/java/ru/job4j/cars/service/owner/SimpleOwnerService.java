package ru.job4j.cars.service.owner;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.*;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleOwnerService implements OwnerService {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Owner> findAll() {
        try {
            return crudRepository.query(
                    "from Owner order by name asc",
                    Owner.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Owner> findById(Long id) {
        try {
            return crudRepository.optional(
                    "from Owner where id = :fId",
                    Owner.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Owner> findByName(String name) {
        try {
            return crudRepository.optional(
                    "from Owner where name = :fName",
                    Owner.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(Owner owner) {
        try {
            crudRepository.run(session -> session.persist(owner));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Optional<Owner> persistIfNotExist(String name) {
        try {
            Optional<Owner> existing = findByName(name);
            return existing.isPresent()
                    ? existing
                    : crudRepository.runFunction(session -> session.merge(new Owner(name)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Owner> persistIfNotExist(List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .map(this::persistIfNotExist)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public boolean update(Owner owner) {
        try {
            return findById(owner.getId())
                    .map(origin -> {
                        origin.setName(owner.getName());
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
                    "delete from Owner where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

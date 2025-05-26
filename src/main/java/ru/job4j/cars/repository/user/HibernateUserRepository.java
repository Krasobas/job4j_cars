package ru.job4j.cars.repository.user;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<User> findAll() {
        try {
            return crudRepository.query(
                    "from User order by name asc",
                    User.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return crudRepository.optional(
                    "from User u where u.id = :fId",
                    User.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) {
        try {
            return crudRepository.optional(
                    "from User u join fetch u.subscribers where u.name = :fName",
                    User.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try {
            return crudRepository.optional(
                    "from User u join fetch u.subscribers where u.email = :fEmail u.password = :fPassword",
                    User.class,
                    Map.of(
                            "fEmail", email,
                            "fPassword", password
                    )
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        try {
            return findById(user.getId())
                    .map(origin -> {
                        origin.setName(user.getName());
                        origin.setSubscriptions(user.getSubscriptions());
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
                    "delete from User where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
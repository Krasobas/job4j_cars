package ru.job4j.cars.repository.user;

import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByName(String name);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean persist(User user);

    boolean update(User user);

    boolean delete(Long id);

  Optional<User> findByEmail(String email);
}

package ru.job4j.cars.repository.post;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Collection<Post> findAll();

    Collection<Post> findByUser(User user);

    Optional<Post> findById(Long id);

    boolean persist(Post post);

    boolean update(Post post);

    boolean delete(Long id);
}

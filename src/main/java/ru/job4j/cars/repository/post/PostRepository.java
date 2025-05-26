package ru.job4j.cars.repository.post;

import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Collection<Post> findAll();

    Collection<Post> findWithPhoto();

    Collection<Post> findByUser(User user);

    Collection<Post> findByBrand(Brand brand);

    Collection<Post> findByCreatedAfter(LocalDateTime time);

    Collection<Post> findByCreatedBefore(LocalDateTime time);

    Optional<Post> findById(Long id);

    boolean persist(Post post);

    boolean update(Post post);

    boolean delete(Long id);
}

package ru.job4j.cars.repository.post;

import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Collection<Post> findAll();

    Collection<Post> findByOwnerId(Long ownerId);

    Collection<Post> findBySubscriberId(Long subscriberId);

    Collection<Post> findByAvailability(boolean isAvailable);

    Collection<Post> findByTitleLike(String title);

    Collection<Post> findWithPhoto();

    Collection<Post> findByBrand(Brand brand);

    Collection<Post> findByCreatedAfter(LocalDateTime time);

    Collection<Post> findByCreatedBefore(LocalDateTime time);

    Optional<Post> findById(Long id);

    boolean persist(Post post);

    boolean update(Post post);

    boolean delete(Post post);

    boolean updateSubscribers(Long id, User user);
}

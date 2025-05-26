package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernatePostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Post> findAll() {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    left join fetch p.photos
                    order by p.created asc p.updated asc p.title asc
                    """,
                    Post.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findWithPhoto() {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    join fetch p.photos
                    order by p.created asc p.updated asc p.title asc
                    """,
                    Post.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByUser(User user) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    left join fetch p.photos
                    where p.user = :fUser
                    order by p.created asc p.updated asc p.title asc
                    """,
                    Post.class,
                    Map.of("fUser", user)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByBrand(Brand brand) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    left join fetch p.photos
                    where p.car.brand = :fBrand
                    order by p.created asc p.updated asc p.title asc
                    """,
                    Post.class,
                    Map.of("fBrand", brand)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByCreatedAfter(LocalDateTime time) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    left join fetch p.photos
                    where p.created >= :fTime
                    order by p.created asc p.updated asc p.title asc
                    """,
                    Post.class,
                    Map.of("fTime", time)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByCreatedBefore(LocalDateTime time) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    left join fetch p.photos
                    where p.created <= :fTime
                    order by p.created asc p.updated asc p.title asc
                    """,
                    Post.class,
                    Map.of("fTime", time)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Post> findById(Long id) {
        try {
            return crudRepository.optional(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.priceHistories
                    left join fetch p.subscribers
                    left join fetch p.photos
                    where p.id = :fId
                    """,
                    Post.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(Post post) {
        try {
            crudRepository.run(session -> session.persist(post));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(Post post) {
        try {
            return findById(post.getId())
                    .map(origin -> {
                        origin.setTitle(post.getTitle());
                        origin.setDescription(post.getDescription());
                        origin.setPrice(post.getPrice());
                        origin.setAvailable(post.getAvailable());
                        origin.setCar(post.getCar());
                        origin.setPhotos(post.getPhotos());
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
                    "delete from Post where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
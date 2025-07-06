package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.*;

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
                    left join fetch p.photos
                    order by p.created asc, p.updated asc, p.title asc
                    """,
                    Post.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByOwnerId(Long ownerId) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.photos
                    where p.user.id = :fOwnerId
                    order by p.created asc, p.updated asc, p.title asc
                    """,
                    Post.class,
                    Map.of("fOwnerId", ownerId)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findBySubscriberId(Long subscriberId) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.photos
                    join p.subscribers subscriber
                    where subscriber.id = :fSubscriberId
                    order by p.created asc, p.updated asc, p.title asc
                    """,
                    Post.class,
                    Map.of("fSubscriberId", subscriberId)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByAvailability(boolean isAvailable) {
        try {
            return crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.photos
                    where p.available = :fAvailable
                    order by p.created asc, p.updated asc, p.title asc
                    """,
                    Post.class,
                    Map.of("fAvailable", isAvailable)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Collection<Post> findByTitleLike(String title) {
        try {
            var result = crudRepository.query(
                    """
                    select distinct p from Post p
                    join fetch p.user
                    join fetch p.car
                    left join fetch p.photos
                    where lower(p.title) like lower(concat('%', :fTitle, '%'))
                    order by p.created asc, p.updated asc, p.title asc
                    """,
                    Post.class,
                    Map.of("fTitle", title)
            );
            System.out.println(result);
            return result;
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
                    join fetch p.photos
                    order by p.created asc, p.updated asc, p.title asc
                    """,
                    Post.class
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
                    left join fetch p.photos
                    where p.car.brand = :fBrand
                    order by p.created asc, p.updated asc, p.title asc
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
                    left join fetch p.photos
                    where p.created >= :fTime
                    order by p.created asc, p.updated asc, p.title asc
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
                    left join fetch p.photos
                    where p.created <= :fTime
                    order by p.created asc, p.updated asc, p.title asc
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
                    left join fetch p.photos
                    left join fetch p.subscribers
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
            crudRepository.run(session -> session.merge(post));
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
    public boolean delete(Post post) {
        try {
            crudRepository.run(session -> session.remove(session.merge(post)));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean updateSubscribers(Long id, User user) {
        try {
            return findById(id)
                    .map(origin -> {
                        if (origin.getSubscribers().contains(user)) {
                            origin.getSubscribers().remove(user);
                        } else {
                            origin.getSubscribers().add(user);
                        }
                        crudRepository.run(session -> session.merge(origin));
                        return origin;
                    }).isPresent();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
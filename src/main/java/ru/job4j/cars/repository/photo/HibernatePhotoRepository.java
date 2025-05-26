package ru.job4j.cars.repository.photo;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Photo;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernatePhotoRepository implements PhotoRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Photo> findAll() {
        try {
            return crudRepository.query(
                    "from Photo p join fetch p.post",
                    Photo.class
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Photo> findById(Long id) {
        try {
            return crudRepository.optional(
                    "from Photo p join fetch p.post where p.id = :fId",
                    Photo.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Photo> findByPath(String path) {
        try {
            return crudRepository.optional(
                    "from Photo p join fetch p.post where p.path = :fPath",
                    Photo.class,
                    Map.of("fPath", path)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(Photo photo) {
        try {
            crudRepository.run(session -> session.persist(photo));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(Photo photo) {
        try {
            if (photo.getMain()) {
                crudRepository.run(
                        "update Photo p set p.main = false where p.id != :fId and p.post.id = :fPostId",
                        Map.of(
                                "fId", photo.getId(),
                                "fPostId", photo.getPost().getId()
                        )
                );
            }
            return findById(photo.getId())
                    .map(origin -> {
                        origin.setPath(photo.getPath());
                        origin.setMain(photo.getMain());
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
                    "delete from Photo where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

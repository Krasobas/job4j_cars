package ru.job4j.cars.service.photo;

import ru.job4j.cars.model.Photo;

import java.util.Collection;
import java.util.Optional;

public interface PhotoService {

    Collection<Photo> findAll();

    Optional<Photo> findById(Long id);

    Optional<Photo> findByPath(String path);

    boolean persist(Photo photo);

    boolean update(Photo photo);

    boolean delete(Long id);
}

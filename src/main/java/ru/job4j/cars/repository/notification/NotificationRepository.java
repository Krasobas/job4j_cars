package ru.job4j.cars.repository.notification;

import ru.job4j.cars.model.Notification;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface NotificationRepository {

    boolean persist(Notification user);

    boolean delete(Long id);

    Collection<Notification> findByUserId(Long userId);
}

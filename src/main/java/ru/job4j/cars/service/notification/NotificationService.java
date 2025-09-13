package ru.job4j.cars.service.notification;

import ru.job4j.cars.dto.notification.NotificationDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.model.Notification;
import ru.job4j.cars.model.Post;

import java.util.Collection;
import java.util.List;

public interface NotificationService {

    boolean persist(Post post, Long oldPrice, Long newPrice);

    boolean delete(Long id);

    List<NotificationDto> findByUserId(UserSessionDto user);
}

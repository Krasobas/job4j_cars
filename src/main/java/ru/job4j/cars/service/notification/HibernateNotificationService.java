package ru.job4j.cars.service.notification;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.notification.NotificationDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.mapstruct.notification.NotificationMapper;
import ru.job4j.cars.model.Notification;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.notification.NotificationRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@JBossLog
@Service
@AllArgsConstructor
public class HibernateNotificationService implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public boolean persist(Post post, Long oldPrice, Long newPrice) {
        Notification notification = new Notification();
        notification.setMessage(String.format("Price of %d went %s", post.getId(), oldPrice > newPrice ? "down" : "up"));
        notification.setRecipients(post.getSubscribers());
        post.getSubscribers().forEach(u -> {
            log.warn(u.getSubscriptions());
        });
        return repository.persist(notification);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public List<NotificationDto> findByUserId(UserSessionDto user) {
        return repository.findByUserId(user.getId()).stream().map(entity -> mapper.getDto(entity, user)).toList();
    }
}
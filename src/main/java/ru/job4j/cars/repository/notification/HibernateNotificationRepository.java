package ru.job4j.cars.repository.notification;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Notification;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Repository
@AllArgsConstructor
public class HibernateNotificationRepository implements NotificationRepository {

    private final CrudRepository crudRepository;

    @Override
    public boolean persist(Notification notification) {
        try {
            crudRepository.run(session -> session.persist(notification));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try {
            return crudRepository.run(
                    "delete from Notification where id = :fId",
                    Map.of("fId", id)
            ) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Collection<Notification> findByUserId(Long userId) {
        try {
            var l = crudRepository.query(
                "from Notification n join fetch n.recipients r where r.id = :fUserId order by n.created asc",
                Notification.class,
                Map.of("fUserId", userId)
            );
            System.out.println(l);
            return l;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }
}
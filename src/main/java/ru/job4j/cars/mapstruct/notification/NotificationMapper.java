package ru.job4j.cars.mapstruct.notification;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.job4j.cars.dto.notification.NotificationDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.mapstruct.post.PostMapper;
import ru.job4j.cars.model.Notification;

@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface NotificationMapper {
    NotificationDto getDto(Notification notification, @Context UserSessionDto userSessionDto);
}

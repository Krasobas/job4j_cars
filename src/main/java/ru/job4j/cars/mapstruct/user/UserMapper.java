package ru.job4j.cars.mapstruct.user;

import org.mapstruct.*;
import ru.job4j.cars.dto.notification.NotificationDto;
import ru.job4j.cars.dto.user.UserCreateDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.mapstruct.post.PostMapper;
import ru.job4j.cars.model.Notification;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSessionDto getDto(User user);

    User getEntityOnCreate(UserCreateDto userDto);
}

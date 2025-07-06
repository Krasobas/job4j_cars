package ru.job4j.cars.mapstruct.user;

import org.mapstruct.Mapper;
import ru.job4j.cars.dto.user.UserCreateDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSessionDto getDto(User user);

    User getEntityOnCreate(UserCreateDto userDto);
}

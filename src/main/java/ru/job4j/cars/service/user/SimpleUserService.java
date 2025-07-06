package ru.job4j.cars.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.user.UserCreateDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.mapstruct.user.UserMapper;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.user.UserRepository;

import java.util.Optional;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public boolean save(UserCreateDto user) {
        User entity = mapper.getEntityOnCreate(user);
        return repository.persist(entity);
    }

    @Override
    public Optional<UserSessionDto> findByEmailAndPassword(String email, String password) {
        return repository.findByEmailAndPassword(email, password)
                .map(mapper::getDto);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }
}

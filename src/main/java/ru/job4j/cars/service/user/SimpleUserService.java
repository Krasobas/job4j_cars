package ru.job4j.cars.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean save(UserCreateDto user) {
        User entity = mapper.getEntityOnCreate(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.persist(entity);
    }

    @Override
    public Optional<UserSessionDto> findByEmailAndPassword(String email, String password) {
        log.info("Attempting login for email: " + email);
        Optional<User> user = repository.findByEmail(email);

        if (user.isPresent()) {
            User foundUser = user.get();
            log.info("User found, checking password for: " + email);

            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                log.info("Password matches for: " + email);
                return Optional.of(mapper.getDto(foundUser));
            } else {
                log.warn("Password mismatch for: " + email);
            }
        } else {
            log.warn("User not found: " + email);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }
}
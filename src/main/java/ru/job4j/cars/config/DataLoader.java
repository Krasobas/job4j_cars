package ru.job4j.cars.config;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.user.UserRepository;

@Component
@AllArgsConstructor
@JBossLog
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    createDefaultUsers();
  }

  private void createDefaultUsers() {
    createUserIfNotExists(
      "Admin User",
      "admin@example.com",
      "admin123",
      "admin"
    );

    createUserIfNotExists(
      "Test User",
      "user@example.com",
      "user123",
      "user"
    );
  }

  private void createUserIfNotExists(String name, String email, String password, String role) {
    if (userRepository.findByEmail(email).isEmpty()) {

      User user = new User();
      user.setName(name);
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(password));
      user.setRole(role);
      user.setTimeZone("Europe/Moscow");

      boolean created = userRepository.persist(user);

      if (created) {
        log.info("Created user: " + email + " with role: " + role);
      } else {
        log.error("Failed to create user: " + email);
      }
    } else {
      log.info("User already exists: " + email);
    }
  }
}
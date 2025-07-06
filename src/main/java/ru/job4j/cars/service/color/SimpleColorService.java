package ru.job4j.cars.service.color;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.color.ColorRepository;

import java.util.Collection;
import java.util.Optional;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleColorService implements ColorService {

    private final ColorRepository repository;

    @Override
    public Collection<Color> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Color> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Color> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Color> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public boolean persist(Color color) {
        return repository.persist(color);
    }

    @Override
    public boolean update(Color color) {
        return repository.update(color);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }
}

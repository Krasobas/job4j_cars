package ru.job4j.cars.service.engine;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.engine.EngineRepository;

import java.util.Collection;
import java.util.Optional;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleEngineService implements EngineService {

    private final EngineRepository repository;

    @Override
    public Collection<Engine> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Engine> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Engine> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public boolean persist(Engine engine) {
        return repository.persist(engine);
    }

    @Override
    public boolean update(Engine engine) {
        return repository.update(engine);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }
}

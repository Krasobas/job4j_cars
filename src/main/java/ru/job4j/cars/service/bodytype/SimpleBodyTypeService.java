package ru.job4j.cars.service.bodytype;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.BodyType;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.bodytype.BodyTypeRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Service
@AllArgsConstructor
public class SimpleBodyTypeService implements BodyTypeService {
    private final BodyTypeRepository repository;

    @Override
    public Collection<BodyType> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<BodyType> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<BodyType> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public boolean persist(BodyType bodyType) {
        return repository.persist(bodyType);
    }

    @Override
    public boolean update(BodyType bodyType) {
        return repository.update(bodyType);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }
}

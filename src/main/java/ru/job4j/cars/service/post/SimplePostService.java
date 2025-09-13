package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.post.PostCreateDto;
import ru.job4j.cars.dto.post.PostEditDto;
import ru.job4j.cars.dto.post.PostListingDto;
import ru.job4j.cars.dto.post.PostViewDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.mapstruct.post.PostMapper;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.service.bodytype.BodyTypeService;
import ru.job4j.cars.service.brand.BrandService;
import ru.job4j.cars.service.color.ColorService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.notification.NotificationService;
import ru.job4j.cars.service.owner.OwnerService;
import ru.job4j.cars.service.storage.FileStorageService;
import ru.job4j.cars.service.user.UserService;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {
    private final PostRepository repository;
    private final PostMapper mapper;
    private final UserService userService;
    private final BrandService brandService;
    private final BodyTypeService bodyTypeService;
    private final EngineService engineService;
    private final ColorService colorService;
    private final OwnerService ownerService;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    @Override
    public Collection<PostListingDto> findAll(UserSessionDto user) {
        return repository.findAll()
                .stream()
                .map(entity -> mapper.getListingDto(entity, user))
                .toList();
    }

    @Override
    public Collection<PostListingDto> findByOwnerId(UserSessionDto user) {
        return repository.findByOwnerId(user.getId())
                .stream()
                .map(entity -> mapper.getListingDto(entity, user))
                .toList();
    }

    @Override
    public Collection<PostListingDto> findBySubscriberId(UserSessionDto user) {
        return repository.findBySubscriberId(user.getId())
                .stream()
                .map(entity -> mapper.getListingDto(entity, user))
                .toList();
    }

    @Override
    public Collection<PostListingDto> findByTitleLike(UserSessionDto user, String title) {
        return repository.findByTitleLike(title)
                .stream()
                .map(entity -> mapper.getListingDto(entity, user))
                .toList();
    }

    @Override
    public Collection<PostListingDto> findByAvailability(UserSessionDto user, boolean isAvailable) {
        return repository.findByAvailability(isAvailable)
                .stream()
                .map(entity -> mapper.getListingDto(entity, user))
                .toList();
    }

    @Override
    public Optional<PostViewDto> findById(Long id, UserSessionDto user) {
        return repository.findById(id)
                .map(entity -> mapper.getViewDto(entity, user));
    }

    @Override
    public Optional<PostEditDto> findByIdOnEdit(Long id, UserSessionDto user) {
        return repository.findById(id)
                .map(entity -> mapper.getEditDto(entity, user));
    }

    @Override
    public boolean persist(PostCreateDto post, UserSessionDto user, MultipartFile[] photosFiles) {
        Optional<User> createdBy = userService.findById(user.getId());
        if (createdBy.isEmpty()) {
            return false;
        }
        Optional<Brand> brand = brandService.findById(post.getBrandId());
        if (brand.isEmpty()) {
            return false;
        }
        Optional<BodyType> bodyType = bodyTypeService.findById(post.getBodyTypeId());
        if (bodyType.isEmpty()) {
            return false;
        }
        Optional<Engine> engine = engineService.findById(post.getEngineId());
        if (engine.isEmpty()) {
            return false;
        }
        Optional<Color> color = colorService.findById(post.getColorId());
        if (color.isEmpty()) {
            return false;
        }
        List<Owner> owners = ownerService.persistIfNotExist(post.getOwners());
        Car car = new Car();
        car.setBrand(brand.get());
        car.setModel(post.getModel());
        car.setBodyType(bodyType.get());
        car.setEngine(engine.get());
        car.setColor(color.get());
        car.setOwners(owners);
        car.setYear(post.getYear());
        car.setMileage(post.getMileage());
        Post entity = mapper.getEntityOnCreate(post, createdBy.get(), car);
        fileStorageService.storeFiles(photosFiles, entity);
        return repository.persist(entity);
    }

    @Override
    public boolean update(PostEditDto post, UserSessionDto user, MultipartFile[] photosFiles) {
        return repository.findById(post.getId())
            .filter(existingPost -> existingPost.getUser().getId().equals(user.getId()))
            .map(existingPost -> {
                Car car = existingPost.getCar();
                updateCarProperty(car::setBrand, car.getBrand().getId(), post.getBrandId(), brandService::findById);
                updateCarProperty(car::setBodyType, car.getBodyType().getId(), post.getBodyTypeId(), bodyTypeService::findById);
                updateCarProperty(car::setEngine, car.getEngine().getId(), post.getEngineId(), engineService::findById);
                updateCarProperty(car::setColor, car.getColor().getId(), post.getColorId(), colorService::findById);
                List<String> currentOwners = car.getOwners().stream().map(Owner::getName).toList();
                if (!currentOwners.equals(post.getOwners())) {
                    car.setOwners(ownerService.persistIfNotExist(post.getOwners()));
                }
                car.setModel(post.getModel());
                car.setMileage(post.getMileage());
                car.setYear(post.getYear());
                existingPost.setAvailable(post.isAvailable());
                existingPost.setTitle(post.getTitle());
                existingPost.setDescription(post.getDescription());
                updatePriceHistory(existingPost, post);
                List<Photo> photosToRemove = existingPost.getPhotos().stream()
                    .filter(photo -> post.getDeletedPhotos().contains(photo.getPath()))
                    .toList();
                photosToRemove.forEach(existingPost.getPhotos()::remove);
                fileStorageService.deleteFiles(post.getDeletedPhotos());
                fileStorageService.storeFiles(photosFiles, existingPost);

                return  repository.update(existingPost);
            })
            .orElse(false);
    }

    private void updatePriceHistory(Post existing, PostEditDto update) {
        long oldPrice = existing.getPrice();
        if (oldPrice == update.getPrice()) {
            return;
        }
        existing.setPrice(update.getPrice());
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPost(existing);
        priceHistory.setBefore(oldPrice);
        priceHistory.setAfter(update.getPrice());
        Set<PriceHistory> history = existing.getHistory();
        if(Objects.isNull(history)) history = new HashSet<>();
        history.add(priceHistory);
        existing.setHistory(history);
        notificationService.persist(existing, oldPrice, update.getPrice());

    }

    private <T> void updateCarProperty(Consumer<T> setter, Long currentId, Long newId, Function<Long, Optional<T>> finder) {
        if (!currentId.equals(newId)) {
            finder.apply(newId).ifPresent(setter);
        }
    }

    @Override
    public boolean toggleSubscribe(Long id, UserSessionDto user) {
        return userService.findById(user.getId())
                .filter(subscriber -> repository.updateSubscribers(id, subscriber))
                .isPresent();
    }

    @Override
    public boolean delete(Long id) {
        Optional<Post> found = repository.findById(id);
        if (found.isEmpty()) {
            return false;
        }
        Post post = found.get();
        Set<String> paths = post.getPhotos()
                .stream()
                .map(Photo::getPath)
                .collect(Collectors.toSet());
        if (repository.delete(post)) {
            fileStorageService.deleteFiles(paths);
            return true;
        }
        return false;
    }
}

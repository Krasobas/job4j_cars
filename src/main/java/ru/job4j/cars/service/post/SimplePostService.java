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
import ru.job4j.cars.service.owner.OwnerService;
import ru.job4j.cars.service.storage.FileStorageService;
import ru.job4j.cars.service.user.UserService;

import java.util.*;
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
        Car car = new Car(brand.get(), post.getModel(), bodyType.get(), engine.get(), color.get(), owners, post.getYear(), post.getMileage());
        Post entity = mapper.getEntityOnCreate(post, createdBy.get(), car);
        fileStorageService.storeFiles(photosFiles, entity);
        return repository.persist(entity);
    }

    @Override
    public boolean update(PostEditDto post, UserSessionDto user, MultipartFile[] photosFiles) {
        Optional<Post> entity = repository.findById(post.getId());
        if (entity.isEmpty()) {
            return false;
        }
        Post toUpdate = entity.get();
        if (!toUpdate.getUser().getId().equals(user.getId())) {
            return  false;
        }
        Car car = toUpdate.getCar();
        if (!car.getBrand().getId().equals(post.getBrandId())) {
            Optional<Brand> brand = brandService.findById(post.getBrandId());
            if (brand.isEmpty()) {
                return false;
            }
            car.setBrand(brand.get());
        }
        if (!car.getBodyType().getId().equals(post.getBodyTypeId())) {
            Optional<BodyType> bodyType = bodyTypeService.findById(post.getBodyTypeId());
            if (bodyType.isEmpty()) {
                return false;
            }
            car.setBodyType(bodyType.get());
        }
        if (!car.getEngine().getId().equals(post.getEngineId())) {
            Optional<Engine> engine = engineService.findById(post.getEngineId());
            if (engine.isEmpty()) {
                return false;
            }
            car.setEngine(engine.get());
        }
        if (!car.getColor().getId().equals(post.getColorId())) {
            Optional<Color> color = colorService.findById(post.getColorId());
            if (color.isEmpty()) {
                return false;
            }
            car.setColor(color.get());
        }
        if (!car.getOwners().stream().map(Owner::getName).toList().equals(post.getOwners())) {
            List<Owner> owners = ownerService.persistIfNotExist(post.getOwners());
            car.setOwners(owners);
        }
        car.setModel(post.getModel());
        car.setMileage(post.getMileage());
        car.setYear(post.getYear());
        toUpdate.setCar(car);
        toUpdate.setAvailable(post.isAvailable());
        toUpdate.setTitle(post.getTitle());
        toUpdate.setDescription(post.getDescription());
        long oldPrice = toUpdate.getPrice();
        long newPrice = post.getPrice();
        if (oldPrice != newPrice) {
            toUpdate.setPrice(newPrice);
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setPost(toUpdate);
            priceHistory.setBefore(oldPrice);
            priceHistory.setAfter(newPrice);
            /*create price history*/
        }

        List<Photo> photosToRemove = toUpdate.getPhotos()
                .stream()
                .filter(photo -> post.getDeletedPhotos().contains(photo.getPath()))
                .toList();
        photosToRemove.forEach(toUpdate.getPhotos()::remove);
        fileStorageService.deleteFiles(post.getDeletedPhotos());
        fileStorageService.storeFiles(photosFiles, toUpdate);

        return repository.update(toUpdate);
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

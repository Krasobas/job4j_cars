package ru.job4j.cars.mapstruct.post;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.cars.dto.post.PostCreateDto;
import ru.job4j.cars.dto.post.PostEditDto;
import ru.job4j.cars.dto.post.PostListingDto;
import ru.job4j.cars.dto.post.PostViewDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "post.id", target = "id")
    @Mapping(source = "post.title", target = "title")
    @Mapping(source = "post.description", target = "description")
    @Mapping(source = "post.price", target = "price")
    @Mapping(source = "post.available", target = "available", defaultValue = "false")
    @Mapping(source = "post.car.brand.name", target = "brand")
    @Mapping(source = "post.car.model", target = "model")
    @Mapping(source = "post.car.bodyType.name", target = "bodyType")
    @Mapping(source = "post.car.engine.name", target = "engine")
    @Mapping(source = "post.car.color.name", target = "color")
    @Mapping(source = "post.car.year", target = "year")
    @Mapping(source = "post.car.mileage", target = "mileage")
    @Mapping(source = "post.photos", target = "photos")
    @Mapping(source = "post.user.id", target = "userId")
    @Mapping(source = "post.created", target = "created")
    @Mapping(source = "post.updated", target = "updated")
    PostListingDto getListingDto(Post post, @Context UserSessionDto user);

    @Mapping(source = "post.id", target = "id")
    @Mapping(source = "post.title", target = "title")
    @Mapping(source = "post.description", target = "description")
    @Mapping(source = "post.price", target = "price")
    @Mapping(source = "post.available", target = "available", defaultValue = "false")
    @Mapping(source = "post.car.brand.name", target = "brand")
    @Mapping(source = "post.car.model", target = "model")
    @Mapping(source = "post.car.bodyType.name", target = "bodyType")
    @Mapping(source = "post.car.engine.name", target = "engine")
    @Mapping(source = "post.car.color.name", target = "color")
    @Mapping(source = "post.car.year", target = "year")
    @Mapping(source = "post.car.mileage", target = "mileage")
    @Mapping(source = "post.car.owners", target = "owners")
    @Mapping(source = "post.photos", target = "photos")
    @Mapping(source = "post.subscribers", target = "subscribersCount")
    @Mapping(source = "post.user.id", target = "userId")
    @Mapping(source = "post.user.name", target = "userName")
    @Mapping(source = "post.user.phone", target = "userPhone")
    @Mapping(source = "post.user.email", target = "userEmail")
    @Mapping(source = "post.subscribers", target = "liked")
    @Mapping(source = "post.created", target = "created")
    @Mapping(source = "post.updated", target = "updated")
    PostViewDto getViewDto(Post post, @Context UserSessionDto user);

    @Mapping(source = "post.id", target = "id")
    @Mapping(source = "post.title", target = "title")
    @Mapping(source = "post.description", target = "description")
    @Mapping(source = "post.price", target = "price")
    @Mapping(source = "post.available", target = "available", defaultValue = "false")
    @Mapping(source = "post.car.brand.id", target = "brandId")
    @Mapping(source = "post.car.model", target = "model")
    @Mapping(source = "post.car.bodyType.id", target = "bodyTypeId")
    @Mapping(source = "post.car.engine.id", target = "engineId")
    @Mapping(source = "post.car.color.id", target = "colorId")
    @Mapping(source = "post.car.year", target = "year")
    @Mapping(source = "post.car.mileage", target = "mileage")
    @Mapping(source = "post.car.owners", target = "owners")
    @Mapping(source = "post.photos", target = "photos")
    PostEditDto getEditDto(Post post, @Context UserSessionDto user);

    @Mapping(source = "postDto.title", target = "title")
    @Mapping(source = "postDto.description", target = "description")
    @Mapping(source = "postDto.price", target = "price")
    @Mapping(source = "postDto.available", target = "available")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(source = "user", target = "user")
    @Mapping(source = "car", target = "car")
    @Mapping(target = "photos", ignore = true)
    Post getEntityOnCreate(PostCreateDto postDto, User user, Car car);

    default List<String> mapPhotoList(Collection<Photo> photos) {
        if (photos == null) {
            return Collections.emptyList();
        }
        return photos.stream()
                .map(Photo::getPath)
                .toList();
    }

    default boolean mapCreated(Collection<User> subscribers, @Context UserSessionDto user) {
        return Objects.nonNull(subscribers) && subscribers.stream().anyMatch(subscriber -> subscriber.getId().equals(user.getId()));
    }

    default ZonedDateTime mapCreated(LocalDateTime created, @Context UserSessionDto user) {
        if (Objects.isNull(created)) {
            created = LocalDateTime.now();
        }
        String userZone = user.getTimeZone();
        ZoneId zoneId = Objects.isNull(userZone) || userZone.isBlank() ? TimeZone.getDefault().toZoneId() : ZoneId.of(userZone);
        return created.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId);
    }

    default List<String> mapOwners(Collection<Owner> owners) {
        return Objects.isNull(owners)
                ? Collections.emptyList()
                : owners.stream()
                        .map(Owner::getName)
                        .toList();
    }

    default int mapSubscribersCount(Collection<User> subscribers) {
        return Objects.isNull(subscribers) ? 0 : subscribers.size();
    }
}

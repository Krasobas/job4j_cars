package ru.job4j.cars.service.post;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.post.PostCreateDto;
import ru.job4j.cars.dto.post.PostEditDto;
import ru.job4j.cars.dto.post.PostListingDto;
import ru.job4j.cars.dto.post.PostViewDto;
import ru.job4j.cars.dto.user.UserSessionDto;

import java.util.Collection;
import java.util.Optional;

public interface PostService {
    Collection<PostListingDto> findAll(UserSessionDto user);

    Collection<PostListingDto> findByOwnerId(UserSessionDto user);

    Collection<PostListingDto> findBySubscriberId(UserSessionDto user);

    Collection<PostListingDto> findByTitleLike(UserSessionDto user, String title);

    Collection<PostListingDto> findByAvailability(UserSessionDto user, boolean isAvailable);

    Optional<PostViewDto> findById(Long id, UserSessionDto user);

    Optional<PostEditDto> findByIdOnEdit(Long id, UserSessionDto user);

    boolean persist(PostCreateDto post, UserSessionDto user, MultipartFile[] photosFiles);

    boolean update(PostEditDto post, UserSessionDto user, MultipartFile[] photosFiles);

    boolean toggleSubscribe(Long id, UserSessionDto user);

    boolean delete(Long id);

}

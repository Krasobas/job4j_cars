package ru.job4j.cars.service.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.model.Photo;
import ru.job4j.cars.model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(@Value("${upload.path}") String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
    }

    public void storeFiles(MultipartFile[] photosFiles, Post post) {
        if (Objects.isNull(photosFiles)) {
            return;
        }
        Arrays.stream(photosFiles)
                .sequential()
                .filter(Objects::nonNull)
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    String filePath = storeFile(file);
                    Photo photo = new Photo();
                    photo.setPath(filePath);
                    photo.setMain(post.getPhotos().isEmpty());
                    photo.setPost(post);
                    return photo;
                })
                .forEach(post.getPhotos()::add);
    }

    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }
            String filename = UUID.randomUUID()
                    + "."
                    + StringUtils.getFilenameExtension(file.getOriginalFilename());
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(filename))
                    .normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public void deleteFiles(Collection<String> paths) {
        paths.forEach(fileName -> {
            try {
                Path path = this.rootLocation.resolve(Paths.get(fileName))
                        .normalize().toAbsolutePath();
                Files.delete(path);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
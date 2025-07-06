package ru.job4j.cars.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostListingDto {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private boolean available;
    private String brand;
    private String model;
    private String bodyType;
    private String engine;
    private String color;
    private Integer year;
    private Integer mileage;
    private List<String> photos;
    private ZonedDateTime created;
    private ZonedDateTime updated;
    private Long userId;
}

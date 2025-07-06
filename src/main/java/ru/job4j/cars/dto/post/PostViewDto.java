package ru.job4j.cars.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostViewDto {
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
    private int subscribersCount;
    private List<String> owners;
    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private boolean liked;
}

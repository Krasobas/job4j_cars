package ru.job4j.cars.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEditDto {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private boolean available;
    private Long brandId;
    private String model;
    private Long bodyTypeId;
    private Long engineId;
    private Long colorId;
    private List<String> owners = new ArrayList<>();
    private Integer year;
    private Integer mileage;
    private List<String> photos = new ArrayList<>();
    private List<String> deletedPhotos = new ArrayList<>();
}

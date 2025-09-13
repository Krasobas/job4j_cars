package ru.job4j.cars.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.Post;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationDto {
    @EqualsAndHashCode.Include
    private Long id;
    private String message;
    private ZonedDateTime created;
}

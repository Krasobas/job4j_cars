package ru.job4j.cars.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.Post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserSessionDto {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String email;
    private String role;
    private String timeZone;
    List<Post> subscriptions = new ArrayList<>();
}

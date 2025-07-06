package ru.job4j.cars.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserCreateDto {
    @EqualsAndHashCode.Include
    private String name;
    @EqualsAndHashCode.Include
    private String email;
    private String password;
    private String timeZone;
}

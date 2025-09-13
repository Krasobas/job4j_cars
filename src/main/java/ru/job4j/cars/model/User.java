package ru.job4j.cars.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

@Entity
@Table(name = "auto_user")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role = "user";
    @Column(name = "timezone")
    private String timeZone = TimeZone.getDefault().getDisplayName();
    @ToString.Exclude
    @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
    private Set<Post> subscriptions = new HashSet<>();
    @ToString.Exclude
    @ManyToMany(mappedBy = "recipients", fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();
}
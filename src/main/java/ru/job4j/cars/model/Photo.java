package ru.job4j.cars.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "photo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String path;
    @Column(name = "is_main")
    private boolean main;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public boolean getMain() {
        return main;
    }
}
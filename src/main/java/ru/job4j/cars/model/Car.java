package ru.job4j.cars.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "car")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "BRAND_ID_FK"))
    private Brand brand;
    private String model;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "body_type_id", foreignKey = @ForeignKey(name = "BODY_TYPE_ID_FK"))
    private BodyType bodyType;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "color_id", foreignKey = @ForeignKey(name = "COLOR_ID_FK"))
    private Color color;
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "history_owner",
            joinColumns = {
                    @JoinColumn(name = "car_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "owner_id")
            }
    )
    private Set<Owner> owners = new HashSet<>();
    @Column(name = "manufacture_year")
    private Integer year;
    private Integer mileage;
}
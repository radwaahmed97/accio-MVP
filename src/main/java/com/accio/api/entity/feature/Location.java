package com.accio.api.entity.feature;

import com.accio.api.entity.DataStoreResource;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location extends DataStoreResource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private int id;

    private double longitude;
    private double latitude;

    @OneToMany(mappedBy = "location", orphanRemoval = true)
    private List<Library> libraries;
}

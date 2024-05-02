package com.accio.api.entity.feature;

import com.accio.api.entity.DataStoreResource;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "library")
public class Library extends DataStoreResource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "library_seq")
    @SequenceGenerator(name = "library_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private int id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @ManyToMany(mappedBy = "libraries")
    private List<Book> books;
}

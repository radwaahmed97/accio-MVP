package com.accio.api.entity.feature;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private int id;

    private String name;

    @ManyToMany
    private List<Library> libraries;
}

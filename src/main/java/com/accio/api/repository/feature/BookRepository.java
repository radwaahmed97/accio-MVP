package com.accio.api.repository.feature;

import com.accio.api.entity.feature.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
    select b from Book b
    where lower(b.name) like CONCAT('%', lower(:term), '%')
""")
    List<Book> findByTerm(String term);
}

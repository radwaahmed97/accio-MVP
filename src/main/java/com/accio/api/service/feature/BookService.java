package com.accio.api.service.feature;

import com.accio.api.controller.request.CreateBookRequest;
import com.accio.api.dto.BookDto;
import com.accio.api.entity.feature.Book;
import com.accio.api.mapper.BookMapper;
import com.accio.api.repository.feature.BookRepository;
import com.accio.api.repository.feature.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final BookMapper bookMapper;
    public void createBook(CreateBookRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setLibraries(libraryRepository.findAllById(request.getLibrariesIds()));
        bookRepository.save(book);
    }

    public List<BookDto> getAllBooks() {
        return bookMapper.convert(bookRepository.findAll());
    }

    public BookDto getCertainBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "this book does not exist"));
        return bookMapper.convert(book);
    }

    public List<BookDto> getBooksByTerm(String term) {
        return bookMapper.convert(bookRepository.findByTerm(term));
    }
}

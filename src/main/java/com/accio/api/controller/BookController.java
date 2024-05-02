package com.accio.api.controller;

import com.accio.api.controller.request.CreateBookRequest;
import com.accio.api.dto.BookDto;
import com.accio.api.dto.LibraryDto;
import com.accio.api.service.feature.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@Tag(name = "Book", description = "Operations To Manage Books")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookController {
    private final BookService bookService;


    @PostMapping
    @Operation(description = "create a new Book")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('Admin')")
    public void createBook(CreateBookRequest request) {
        bookService.createBook(request);
    }

    @GetMapping
    @Operation(description = "get all books")
    public List<BookDto> getAll() {
        return bookService.getAllBooks();
    }
    @GetMapping("{id}")
    @Operation(description = "get a certain book by id")
    public BookDto getCertain(@PathVariable(value = "id") int id) {
        return bookService.getCertainBook(id);
    }

    @GetMapping("search")
    @Operation(description = "get all books by term")
    public List<BookDto> getByTerm(@RequestParam(value = "term") String term) {
        return bookService.getBooksByTerm(term);
    }

}

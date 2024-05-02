package com.accio.api.controller;

import com.accio.api.controller.request.CreateLibraryRequest;
import com.accio.api.dto.LibraryDto;
import com.accio.api.entity.feature.Library;
import com.accio.api.service.feature.LibraryService;
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
@RequestMapping("api/library")
@Tag(name = "Library", description = "Operations To Manage Libraries")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping
    @Operation(description = "create a new Library")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('Admin')")
    public void createLibrary(@RequestBody CreateLibraryRequest request) {
        libraryService.createLibraryRequest(request);
    }

    @GetMapping
    @Operation(description = "get all libraries")
    public List<LibraryDto> getAll() {
        return libraryService.getAll();
    }
    @GetMapping("{id}")
    @Operation(description = "get a certain library by id")
    public LibraryDto getCertain(@PathVariable(value = "id") int id) {
        return libraryService.getCertainLibrary(id);
    }

    @GetMapping("closest")
    @Operation(description = "get a closest library by book name")
    public List<LibraryDto> getClosestLibraries(@RequestParam(value = "term") String term,
                                                @RequestParam(value = "longitude") double longitude,
                                                @RequestParam(value = "latitude") double latitude) {
        return libraryService.getClosestLibrary(term, longitude, latitude);
    }
}

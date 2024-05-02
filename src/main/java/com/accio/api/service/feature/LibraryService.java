package com.accio.api.service.feature;

import com.accio.api.controller.request.CreateLibraryRequest;
import com.accio.api.dto.BookDto;
import com.accio.api.dto.LibraryDto;
import com.accio.api.entity.feature.Library;
import com.accio.api.entity.feature.Location;
import com.accio.api.mapper.LibraryMapper;
import com.accio.api.repository.feature.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final LocationService locationService;
    private final LibraryMapper libraryMapper;
    private final BookService bookService;
    private static final double EARTH_RADIUS = 6371; // Earth radius in kilometers
    public void createLibraryRequest(CreateLibraryRequest request) {
        Library library = new Library();
        Location location = locationService.create(request.getLocation());
        library.setName(request.getName());
        library.setLocation(location);
        libraryRepository.save(library);
    }

    public List<LibraryDto> getAll() {
        return libraryMapper.convert(libraryRepository.findAll());
    }

    public LibraryDto getCertainLibrary(int id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "this library does not exist"));
        return libraryMapper.convert(library);
    }

    public List<LibraryDto> getClosestLibrary(String term, double longitude, double latitude) {
        List<BookDto> books = bookService.getBooksByTerm(term);
        List<LibraryDto> libraries = new ArrayList<>();
        books.forEach(bookDto -> libraries.addAll(bookDto.getLibraries()));

        LibraryDto closestLibrary = libraries.stream()
                .min(Comparator.comparingDouble(
                        library -> calculateDistance(longitude, latitude, library.getLocation().getLongitude(),
                                library.getLocation().getLatitude())))
                .orElse(null);

        List<LibraryDto> closestLibraries = new ArrayList<>();
        if (closestLibrary != null) {
            closestLibraries.add(closestLibrary);
        }

        return closestLibraries;
    }

    private double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        double lon1Rad = Math.toRadians(lon1);
        double lat1Rad = Math.toRadians(lat1);
        double lon2Rad = Math.toRadians(lon2);
        double lat2Rad = Math.toRadians(lat2);

        double lonDiff = lon2Rad - lon1Rad;
        double latDiff = lat2Rad - lat1Rad;

        double a = Math.pow(Math.sin(latDiff / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(lonDiff / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}

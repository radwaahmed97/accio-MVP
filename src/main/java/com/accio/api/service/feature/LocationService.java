package com.accio.api.service.feature;

import com.accio.api.controller.request.CreateLocationRequest;
import com.accio.api.entity.feature.Location;
import com.accio.api.repository.feature.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;


    public Location create(CreateLocationRequest request) {
        Location location = new Location();
        location.setLatitude(request.getLatitude());
        location.setLongitude(location.getLongitude());

        locationRepository.save(location);
        return location;
    }
}

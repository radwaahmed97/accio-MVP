package com.accio.api.repository.feature;

import com.accio.api.entity.feature.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}

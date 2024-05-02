package com.accio.api.repository.feature;

import com.accio.api.entity.feature.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer> {
}

package com.accio.api.mapper;

import com.accio.api.dto.LibraryDto;
import com.accio.api.entity.feature.Library;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface LibraryMapper {

    LibraryDto convert(Library library);

    List<LibraryDto> convert(List<Library> libraries);
}

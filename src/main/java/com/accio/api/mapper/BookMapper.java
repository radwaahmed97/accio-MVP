package com.accio.api.mapper;

import com.accio.api.dto.BookDto;
import com.accio.api.entity.feature.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookDto convert(Book book);

    List<BookDto> convert(List<Book> books);
}

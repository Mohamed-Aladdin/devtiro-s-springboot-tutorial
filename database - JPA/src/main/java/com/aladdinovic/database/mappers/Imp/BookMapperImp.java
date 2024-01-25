package com.aladdinovic.database.mappers.Imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.aladdinovic.database.domain.dto.BookDto;
import com.aladdinovic.database.domain.entities.BookEntity;
import com.aladdinovic.database.mappers.Mapper;

@Component
public class BookMapperImp implements Mapper<BookEntity, BookDto> {

  private ModelMapper modelMapper;

  public BookMapperImp(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public BookDto mapTo(BookEntity bookEntity) {
    return modelMapper.map(bookEntity, BookDto.class);
  }

  @Override
  public BookEntity mapFrom(BookDto bookDto) {
    return modelMapper.map(bookDto, BookEntity.class);
  }
}

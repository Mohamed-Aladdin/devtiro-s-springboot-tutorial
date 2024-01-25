package com.aladdinovic.database.mappers.Imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.aladdinovic.database.domain.dto.AuthorDto;
import com.aladdinovic.database.domain.entities.AuthorEntity;
import com.aladdinovic.database.mappers.Mapper;

@Component
public class AuthorMapperImp implements Mapper<AuthorEntity, AuthorDto> {

  private ModelMapper modelMapper;

  public AuthorMapperImp(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public AuthorDto mapTo(AuthorEntity authorEntity) {
    return modelMapper.map(authorEntity, AuthorDto.class);
  }

  @Override
  public AuthorEntity mapFrom(AuthorDto authorDto) {
    return modelMapper.map(authorDto, AuthorEntity.class);
  }
}

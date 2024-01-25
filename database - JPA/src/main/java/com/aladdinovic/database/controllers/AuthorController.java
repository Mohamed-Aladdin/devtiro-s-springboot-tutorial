package com.aladdinovic.database.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.aladdinovic.database.domain.dto.AuthorDto;
import com.aladdinovic.database.domain.entities.AuthorEntity;
import com.aladdinovic.database.mappers.Mapper;
import com.aladdinovic.database.services.AuthorService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class AuthorController {
  private AuthorService authorService;

  private Mapper<AuthorEntity, AuthorDto> authorMapper;

  public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
    this.authorService = authorService;
    this.authorMapper = authorMapper;
  }

  @PostMapping(path = "/authors")
  public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
    AuthorEntity authorEntity = authorMapper.mapFrom(author);
    AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
    return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
  }

  @GetMapping("/authors")
  public List<AuthorDto> listAuthors() {
    List<AuthorEntity> authors = authorService.findAll();

    return authors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
  }

  @GetMapping("/authors/{id}")
  public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id) {
    Optional<AuthorEntity> foundAuthor = authorService.findOne(id);

    return foundAuthor.map(authorEntity -> {
      AuthorDto authorDto = authorMapper.mapTo(authorEntity);

      return new ResponseEntity<>(authorDto, HttpStatus.OK);
    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping(path = "/authors/{id}")
  public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
    if (!authorService.isExists(id)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    authorDto.setId(id);
    AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
    AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
    return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.OK);
  }

  @PatchMapping(path = "/authors/{id}")
  public ResponseEntity<AuthorDto> partialUpdate(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
    if (!authorService.isExists(id)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
    AuthorEntity updatedAuthor = authorService.partialUpdate(id, authorEntity);

    return new ResponseEntity<>(authorMapper.mapTo(updatedAuthor), HttpStatus.OK);
  }

  @DeleteMapping(path = "/authors/{id}")
  public ResponseEntity deleteAuthor(@PathVariable("id") Long id) {
    authorService.delete(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}

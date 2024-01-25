package com.aladdinovic.database.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.aladdinovic.database.domain.dto.BookDto;
import com.aladdinovic.database.domain.entities.BookEntity;
import com.aladdinovic.database.mappers.Mapper;
import com.aladdinovic.database.services.BookService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
public class BookController {
  private BookService bookService;

  private Mapper<BookEntity, BookDto> bookMapper;

  public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {
    this.bookService = bookService;
    this.bookMapper = bookMapper;
  }

  @PutMapping(path = "/books/{isbn}")
  public ResponseEntity<BookDto> createBook(@PathVariable String isbn, @RequestBody BookDto bookDto) {
    BookEntity bookEntity = bookMapper.mapFrom(bookDto);
    BookEntity savedBookEntity = bookService.save(isbn, bookEntity);
    BookDto savedUpdatedBookDto = bookMapper.mapTo(savedBookEntity);

    if (!bookService.isExists(isbn)) {
      return new ResponseEntity<>(savedUpdatedBookDto, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(savedUpdatedBookDto, HttpStatus.OK);
    }
  }

  @GetMapping("/books")
  public List<BookDto> listBooks() {
    List<BookEntity> books = bookService.findAll();

    return books.stream().map(bookMapper::mapTo).collect(Collectors.toList());
  }

  //Pages Query
  // @GetMapping("/books")
  // public Page<BookDto> listBooks(Pageable pageable) {
  //   Page<BookEntity> books = bookService.findAllPages(pageable);

  //   return books.map(bookMapper::mapTo);
  // }

  @GetMapping("/books/{isbn}")
  public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
    Optional<BookEntity> book = bookService.findOne(isbn);

    return book.map(bookEntity -> {
      BookDto bookDto = bookMapper.mapTo(bookEntity);

      return new ResponseEntity<>(bookDto, HttpStatus.OK);
    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PatchMapping("/books/{isbn}")
  public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
    if (!bookService.isExists(isbn)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    BookEntity bookEntity = bookMapper.mapFrom(bookDto);
    BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);

    return new ResponseEntity<>(bookMapper.mapTo(updatedBookEntity), HttpStatus.OK);
  }

  @DeleteMapping(path = "/books/{isbn}")
  public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {
    bookService.delete(isbn);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}

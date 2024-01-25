package com.aladdinovic.database.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aladdinovic.database.domain.entities.BookEntity;

public interface BookService {
  BookEntity save(String isbn, BookEntity bookEntity);

  List<BookEntity> findAll();

  Page<BookEntity> findAllPages(Pageable pageable);

  Optional<BookEntity> findOne(String isbn);

  boolean isExists(String isbn);

  BookEntity partialUpdate(String isbn, BookEntity bookEntity);

  void delete(String isbn);
}

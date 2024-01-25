package com.aladdinovic.database;

import com.aladdinovic.database.domain.dto.AuthorDto;
import com.aladdinovic.database.domain.dto.BookDto;
import com.aladdinovic.database.domain.entities.AuthorEntity;
import com.aladdinovic.database.domain.entities.BookEntity;

public final class TestDataUtil {
  private TestDataUtil() {
  }

  public static AuthorEntity createTestAuthorA() {
    return AuthorEntity.builder()
        .id(1L)
        .name("Abigail Rose")
        .age(80)
        .build();
  }

  public static AuthorDto createTestAuthorDtoA() {
    return AuthorDto.builder()
        .id(1L)
        .name("Abigail Rose")
        .age(80)
        .build();
  }

  public static AuthorEntity createTestAuthorB() {
    return AuthorEntity.builder()
        .id(2L)
        .name("Thomas Cronin")
        .age(44)
        .build();
  }

  public static AuthorEntity createTestAuthorC() {
    return AuthorEntity.builder()
        .id(3L)
        .name("Jesse A Casey")
        .age(24)
        .build();
  }

  public static BookEntity createTestBookA(final AuthorEntity author) {
    return BookEntity.builder()
        .isbn("978-1-2345-6789-0")
        .title("The Shadow in the Attic")
        .author(author)
        .build();
  }

  public static BookDto createTestBookDtoA(final AuthorDto authorDto) {
    return BookDto.builder()
        .isbn("978-1-2345-6789-0")
        .title("The Shadow in the Attic")
        .author(authorDto)
        .build();
  }

  public static BookEntity createTestBookB(final AuthorEntity author) {
    return BookEntity.builder()
        .isbn("978-1-2345-6789-1")
        .title("Beyond the Horizon")
        .author(author)
        .build();
  }

  public static BookEntity createTestBookC(final AuthorEntity author) {
    return BookEntity.builder()
        .isbn("978-1-2345-6789-2")
        .title("The Last Ember")
        .author(author)
        .build();
  }
}

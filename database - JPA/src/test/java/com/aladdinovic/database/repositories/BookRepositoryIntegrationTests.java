package com.aladdinovic.database.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import com.aladdinovic.database.TestDataUtil;
import com.aladdinovic.database.domain.entities.AuthorEntity;
import com.aladdinovic.database.domain.entities.BookEntity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {
  private BookRepository underTest;

  @Autowired
  public BookRepositoryIntegrationTests(BookRepository underTest) {
    this.underTest = underTest;
  }

  @Test
  public void testThatBookCanBeCreatedAndRecalled() {
    AuthorEntity author = TestDataUtil.createTestAuthorA();
    BookEntity book = TestDataUtil.createTestBookA(author);
    underTest.save(book);
    Optional<BookEntity> result = underTest.findById(book.getIsbn());
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(book);
  }

  @Test
  public void testThatMultipleBooksCanBeCreatedAndRecalled() {
    AuthorEntity author = TestDataUtil.createTestAuthorA();

    BookEntity bookA = TestDataUtil.createTestBookA(author);
    underTest.save(bookA);

    BookEntity bookB = TestDataUtil.createTestBookB(author);
    underTest.save(bookB);

    BookEntity bookC = TestDataUtil.createTestBookC(author);
    underTest.save(bookC);

    Iterable<BookEntity> result = underTest.findAll();
    assertThat(result).hasSize(3).containsExactly(bookA, bookB, bookC);
  }

  @Test
  public void testThatBookCanBeUpdated() {
    AuthorEntity author = TestDataUtil.createTestAuthorA();

    BookEntity bookA = TestDataUtil.createTestBookA(author);
    underTest.save(bookA);

    bookA.setTitle("Updated");
    underTest.save(bookA);

    Optional<BookEntity> result = underTest.findById(bookA.getIsbn());
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(bookA);
  }

  @Test
  public void testThatBookCanBeDeleted() {
    AuthorEntity author = TestDataUtil.createTestAuthorA();

    BookEntity bookA = TestDataUtil.createTestBookA(author);
    underTest.save(bookA);
    underTest.deleteById(bookA.getIsbn());

    Optional<BookEntity> result = underTest.findById(bookA.getIsbn());
    assertThat(result).isEmpty();
  }
}

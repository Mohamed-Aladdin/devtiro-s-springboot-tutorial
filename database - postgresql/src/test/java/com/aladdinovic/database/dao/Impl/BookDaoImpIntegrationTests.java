package com.aladdinovic.database.dao.Impl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import com.aladdinovic.database.TestDataUtil;
import com.aladdinovic.database.dao.AuthorDao;
import com.aladdinovic.database.dao.impl.BookDaoImp;
import com.aladdinovic.database.domain.Author;
import com.aladdinovic.database.domain.Book;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookDaoImpIntegrationTests {
  private AuthorDao authorDao;
  private BookDaoImp underTest;

  @Autowired
  public BookDaoImpIntegrationTests(BookDaoImp underTest, AuthorDao authorDao) {
    this.underTest = underTest;
    this.authorDao = authorDao;
  }

  @Test
  public void testThatBookCanBeCreatedAndRecalled() {
    Author author = TestDataUtil.createTestAuthorA();
    authorDao.create(author);
    Book book = TestDataUtil.createTestBookA();
    book.setAuthorId(author.getId());
    underTest.create(book);
    Optional<Book> result = underTest.findOne(book.getIsbn());
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(book);
  }

  @Test
  public void testThatMultipleBooksCanBeCreatedAndRecalled() {
    Author author = TestDataUtil.createTestAuthorA();
    authorDao.create(author);

    Book bookA = TestDataUtil.createTestBookA();
    bookA.setAuthorId(author.getId());
    underTest.create(bookA);

    Book bookB = TestDataUtil.createTestBookB();
    bookB.setAuthorId(author.getId());
    underTest.create(bookB);

    Book bookC = TestDataUtil.createTestBookC();
    bookC.setAuthorId(author.getId());
    underTest.create(bookC);

    List<Book> result = underTest.find();
    assertThat(result).hasSize(3).containsExactly(bookA, bookB, bookC);
  }

  @Test
  public void testThatBookCanBeUpdated() {
    Author author = TestDataUtil.createTestAuthorA();
    authorDao.create(author);

    Book bookA = TestDataUtil.createTestBookA();
    bookA.setAuthorId(author.getId());
    underTest.create(bookA);

    bookA.setTitle("Updated");
    underTest.update(bookA.getIsbn(), bookA);

    Optional<Book> result = underTest.findOne(bookA.getIsbn());
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(bookA);
  }

  @Test
  public void testThatBookCanBeDeleted() {
    Author author = TestDataUtil.createTestAuthorA();
    authorDao.create(author);

    Book bookA = TestDataUtil.createTestBookA();
    bookA.setAuthorId(author.getId());
    underTest.create(bookA);
    underTest.delete(bookA.getIsbn());

    Optional<Book> result = underTest.findOne(bookA.getIsbn());
    assertThat(result).isEmpty();
  }
}

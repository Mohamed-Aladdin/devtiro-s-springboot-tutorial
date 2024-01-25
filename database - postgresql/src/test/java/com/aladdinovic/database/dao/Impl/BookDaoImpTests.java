package com.aladdinovic.database.dao.Impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.aladdinovic.database.TestDataUtil;
import com.aladdinovic.database.dao.impl.BookDaoImp;
import com.aladdinovic.database.domain.Book;

@ExtendWith(MockitoExtension.class)
public class BookDaoImpTests {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private BookDaoImp underTest;

  @Test
  public void testThatCreateBookGeneratesCorrectSql() {
    Book book = TestDataUtil.createTestBookA();

    underTest.create(book);

    verify(jdbcTemplate).update(
        eq("INSERT INTO books (isbn, title, author_id) VALUES (?, ?, ?)"),
        eq("978-1-2345-6789-0"),
        eq("The Shadow in the Attic"),
        eq(1L));
  }

  @Test
  public void testThatFindOneBookGeneratesTheCorrectSql() {
    underTest.findOne("978-1-2345-6789-0");

    verify(jdbcTemplate).query(
        eq("SELECT isbn, title, author_id FROM books WHERE isbn = ? LIMIT 1"),
        ArgumentMatchers.<BookDaoImp.BookRowMapper>any(),
        eq("978-1-2345-6789-0"));
  }

  @Test
  public void testThatFindGeneratesCorrectSql() {
    underTest.find();

    verify(jdbcTemplate).query(
        eq("SELECT isbn, title, author_id FROM books"),
        ArgumentMatchers.<BookDaoImp.BookRowMapper>any());
  }

  @Test
  public void testThatUpdateGeneratesCorrectSql() {
    Book book = TestDataUtil.createTestBookA();
    underTest.update("978-1-2345-6789-0", book);

    verify(jdbcTemplate).update(
        "UPDATE books set isbn = ?, title = ?, author_id = ? WHERE isbn = ?",
        "978-1-2345-6789-0", "The Shadow in the Attic", 1L, "978-1-2345-6789-0");
  }

  @Test
  public void testThatDeleteGeneratesTheCorrectSql() {
    underTest.delete("978-1-2345-6789-0");

    verify(jdbcTemplate).update(
        "DELETE FROM books where isbn = ?",
        "978-1-2345-6789-0");
  }
}

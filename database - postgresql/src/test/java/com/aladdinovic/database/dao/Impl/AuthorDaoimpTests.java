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
import com.aladdinovic.database.dao.impl.AuthorDaoImp;
import com.aladdinovic.database.domain.Author;

@ExtendWith(MockitoExtension.class)
public class AuthorDaoimpTests {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private AuthorDaoImp underTest;

  @Test
  public void testThatCreateAuthorGeneratesCorrectSql() {
    Author author = TestDataUtil.createTestAuthorA();

    underTest.create(author);

    verify(jdbcTemplate).update(
        eq("INSERT INTO authors (id, name, age) VALUES (?, ?, ?)"),
        eq(1L), eq("Abigail Rose"), eq(80));
  }

  @Test
  public void testThatFindOneGeneratesTheCorrectSql() {
    underTest.findOne(1L);

    verify(jdbcTemplate).query(
        eq("SELECT id, name, age FROM authors WHERE id = ? LIMIT 1"),
        ArgumentMatchers.<AuthorDaoImp.AuthorRowMapper>any(),
        eq(1L));
  }

  @Test
  public void testThatFindManyGeneratesCorrectSql() {
    underTest.find();

    verify(jdbcTemplate).query(
        eq("SELECT id, name, age FROM authors"),
        ArgumentMatchers.<AuthorDaoImp.AuthorRowMapper>any());
  }

  @Test
  public void testThatUpdateGeneratesCorrectSql() {
    Author author = TestDataUtil.createTestAuthorA();
    underTest.update(3L, author);

    verify(jdbcTemplate).update(
        "UPDATE authors set id = ?, name = ?, age = ? WHERE id = ?",
        1L, "Abigail Rose", 80, 3L);
  }

  @Test
  public void testThatDeleteGeneratesTheCorrectSql() {
    underTest.delete(1L);

    verify(jdbcTemplate).update(
        "DELETE FROM authors where id = ?",
        1L);
  }
}

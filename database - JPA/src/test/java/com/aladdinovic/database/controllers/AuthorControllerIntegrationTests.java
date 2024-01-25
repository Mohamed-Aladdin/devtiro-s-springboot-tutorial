package com.aladdinovic.database.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.aladdinovic.database.TestDataUtil;
import com.aladdinovic.database.domain.dto.AuthorDto;
import com.aladdinovic.database.domain.entities.AuthorEntity;
import com.aladdinovic.database.services.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  private AuthorService authorService;

  @Autowired
  public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
    this.authorService = authorService;
  }

  @Test
  public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
    AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
    testAuthorA.setId(null);
    String authorJson = objectMapper.writeValueAsString(testAuthorA);

    mockMvc.perform(
        MockMvcRequestBuilders.post("/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson)).andExpect(
            MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
    AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
    testAuthorA.setId(null);
    String authorJson = objectMapper.writeValueAsString(testAuthorA);

    mockMvc.perform(
        MockMvcRequestBuilders.post("/authors").contentType(MediaType.APPLICATION_JSON).content(authorJson)).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(80));
  }

  @Test
  public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/authors").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
    AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
    authorService.save(testAuthorA);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/authors").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(80));
  }

  @Test
  public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExist() throws Exception {
    AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
    authorService.save(testAuthorA);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/authors/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testThatGetAuthorReturnsAuthorWhenAuthorExist() throws Exception {
    AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
    authorService.save(testAuthorA);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/authors/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(80));
  }

  @Test
  public void testThatGetAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/authors/999").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
    AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
    String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

    mockMvc.perform(
        MockMvcRequestBuilders.put("/authors/99").contentType(MediaType.APPLICATION_JSON).content(authorDtoJson))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
    AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
    AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

    AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
    String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

    mockMvc.perform(
        MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(authorDtoJson))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
    AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorB();
    AuthorEntity savedAuthor = authorService.save(testAuthorEntityB);

    AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
    testAuthorDtoA.setId(savedAuthor.getId());
    String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

    mockMvc.perform(
        MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(authorDtoJson))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDtoA.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoA.getAge()));
  }

  @Test
  public void testThatPartialUpdateExistingAuthorReturnsHttpStatus20Ok() throws Exception {
    AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorB();
    AuthorEntity savedAuthor = authorService.save(testAuthorEntityB);

    AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
    testAuthorDtoA.setName("Updated");
    String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

    mockMvc.perform(
        MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(authorDtoJson))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
    AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorB();
    AuthorEntity savedAuthor = authorService.save(testAuthorEntityB);

    AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
    testAuthorDtoA.setName("Updated");
    String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

    mockMvc.perform(
        MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(authorDtoJson))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDtoA.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoA.getAge()));
  }

  @Test
  public void testThatDeleteAuthorReturnsHttpStatus204ForNonExistingAuthor() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.delete("/authors/999").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception {
    AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
    AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }
}

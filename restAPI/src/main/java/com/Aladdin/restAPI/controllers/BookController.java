package com.Aladdin.restAPI.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.Aladdin.restAPI.domain.Book;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Log
public class BookController {
  @GetMapping("/books")
  public Book retrieveBook() {
    return Book.builder()
        .isbn("978-0-13-478627-5")
        .title("The Enigma of Eternity")
        .author("Aria Montgomery")
        .yearPublished("2005")
        .build();
  }

  @PostMapping("/books")
  public Book createBook(@RequestBody final Book book) {
    log.info("Got book: " + book.toString());

    return book;
  }
}

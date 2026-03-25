package com.rb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rb.exception.BookException;
import com.rb.paylode.dto.BookDTO;
import com.rb.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/books")
public class AdminBookController {
	
	private final BookService bookService;
	
	@PostMapping
	public ResponseEntity<BookDTO> createBook(
	        @Valid @RequestBody BookDTO bookDTO) throws BookException {
	    BookDTO savedBook = bookService.createBook(bookDTO);
	    return ResponseEntity.ok(savedBook);
	}

}

package com.rb.service;

import java.util.List;

import com.rb.exception.BookException;
import com.rb.paylode.dto.BookDTO;
import com.rb.paylode.request.BookSearchRequest;
import com.rb.paylode.response.PageResponse;

public interface BookService {
	BookDTO createBook(BookDTO bookDTO) throws BookException;
	
	List<BookDTO> createBooksBulk(List<BookDTO> bookDTOs) throws BookException;
	
	BookDTO getBookById(Long bookId) throws BookException;
	
	BookDTO getBookByISBN(String isbn) throws BookException;
	
	BookDTO updateBook(Long bookId,BookDTO bookDTO) throws BookException;
	
	void deleteBook(Long bookId) throws BookException;
	
	void hardDeleteBook(Long bookId) throws BookException;
	
	PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest searchRequest);
	
	long getTotalActiveBooks();
	
	long getTotalAvailableBooks();
	
}

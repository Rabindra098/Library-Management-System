package com.rb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rb.exception.BookException;
import com.rb.paylode.dto.BookDTO;
import com.rb.paylode.request.BookSearchRequest;
import com.rb.paylode.response.APIResponse;
import com.rb.paylode.response.PageResponse;
import com.rb.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
	
	private final BookService bookService;
	
	@PostMapping("/bulk")
	public ResponseEntity<List<BookDTO>> createBooksBulk(
	        @Valid @RequestBody List<BookDTO> bookDTOs) throws BookException {

	    List<BookDTO> createdBooks =
	            bookService.createBooksBulk(bookDTOs);

	    return ResponseEntity.ok(createdBooks);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) throws BookException{
		BookDTO book = bookService.getBookById(id);
		return ResponseEntity.ok(book);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<BookDTO> updateBook(
			@PathVariable Long id,@Valid @RequestBody BookDTO bookDTO) throws BookException{
		BookDTO updateBook = bookService.updateBook(id,bookDTO);
		return ResponseEntity.ok(updateBook);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<APIResponse> deleteBook(@PathVariable Long id) throws BookException{
		bookService.deleteBook(id);
		return ResponseEntity.ok(new APIResponse("Book deleted Successfully",true));
	}
	
	@DeleteMapping("/{id}/hard")
	public ResponseEntity<APIResponse> hardDeleteBook(@PathVariable Long id) throws BookException{
		bookService.hardDeleteBook(id);
		return ResponseEntity.ok(new APIResponse("Book permanently deleted Successfully",true));
	}
	
	@GetMapping
	public ResponseEntity<PageResponse<BookDTO>> searchBooks(
	        @RequestParam(required = false) Long genreId,
	        @RequestParam(required = false,defaultValue = "false") Boolean availableOnly,
	        @RequestParam(defaultValue = "true") boolean activeOnly,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "20") int size,
	        @RequestParam(defaultValue = "createdAt") String sortBy,
	        @RequestParam(defaultValue = "DESC") String sortDirection) {

	    BookSearchRequest searchRequest = new BookSearchRequest();
	    searchRequest.setGenreId(genreId);
	    searchRequest.setAvableOnly(availableOnly);
	    searchRequest.setPage(page);
	    searchRequest.setSize(size);
	    searchRequest.setSortBy(sortBy);
	    searchRequest.setSortDirection(sortDirection);

	    PageResponse<BookDTO> books =
	            bookService.searchBooksWithFilters(searchRequest);

	    return ResponseEntity.ok(books);
	}

	
	@PostMapping("/search")
	public ResponseEntity<PageResponse<BookDTO>> advancedSearch(
	        @Valid @RequestBody BookSearchRequest searchRequest) {
	    PageResponse<BookDTO> books =
	            bookService.searchBooksWithFilters(searchRequest);
	    return ResponseEntity.ok(books);
	}
	
	@GetMapping("/stats")
	public ResponseEntity<BookStatsResponse> getBookStats() {

	    long totalActive = bookService.getTotalActiveBooks();
	    long totalAvailable = bookService.getTotalAvailableBooks();
	    BookStatsResponse stats =
	            new BookStatsResponse(totalActive, totalAvailable);
	    return ResponseEntity.ok(stats);
	}

	public static class BookStatsResponse {

	    private long totalActiveBooks;
	    private long totalAvailableBooks;

	    public BookStatsResponse(long totalActiveBooks, long totalAvailableBooks) {
	        this.totalActiveBooks = totalActiveBooks;
	        this.totalAvailableBooks = totalAvailableBooks;
	    }
	    public long getTotalActiveBooks() {
	        return totalActiveBooks;
	    }
	    public long getTotalAvailableBooks() {
	        return totalAvailableBooks;
	    }
	}


	
}

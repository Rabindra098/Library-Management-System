package com.rb.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false,unique = true)
	private String isbn;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String author;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Genre genre;

	private String publisher;
	
	private LocalDate publishedDate;
	
	private String language;
	
	private Integer pages;
	
	private String description;
	
	@Column(nullable = false)
	private Integer totalCopies;
	
	@Column(nullable = false)
	private Integer availableCopies;
	
	private BigDecimal price;
	
	private String coverImageUrl;
	
	@Column(nullable = false)
	private Boolean active=true;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@AssertTrue(message = "Avaliable copies cannot exced total copies")
	public boolean isAvailableCopiesValid() {
		if(totalCopies==null || availableCopies ==null)
			return true;
		return availableCopies<=totalCopies;
	}
}













package com.rb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Genre code is mandatory")
    private String code;

    @NotBlank(message = "Genre name is mandatory")
    private String name;

    @Size(max = 500)
    private String description;

    @Min(0)
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;

    // Owning side
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_genre_id")
    private Genre parentGenra;

    // Inverse side
    @OneToMany(mappedBy = "parentGenra", cascade = CascadeType.ALL)
    private List<Genre> subGenre = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

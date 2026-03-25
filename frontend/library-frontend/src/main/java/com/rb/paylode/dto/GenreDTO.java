package com.rb.paylode.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreDTO {

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @Size(max = 500)
    private String description;

    @Min(0)
    private Integer displayOrder = 0;

    private Boolean active;

    private Long parentGenreId;
    private String parentGenreName;

    private List<GenreDTO> subGenre;

    private Long bookCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

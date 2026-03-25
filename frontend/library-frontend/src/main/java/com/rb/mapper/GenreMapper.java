package com.rb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rb.model.Genre;
import com.rb.paylode.dto.GenreDTO;
import com.rb.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreMapper {

    private final GenreRepository genreRepository;

    public GenreDTO toDTO(Genre genre) {
        if (genre == null) return null;

        GenreDTO dto = GenreDTO.builder()
                .id(genre.getId())
                .code(genre.getCode())
                .name(genre.getName())
                .description(genre.getDescription())
                .displayOrder(genre.getDisplayOrder())
                .active(genre.getActive())
                .createdAt(genre.getCreatedAt())
                .updatedAt(genre.getUpdatedAt())
                .build();

        if (genre.getParentGenra() != null) {
            dto.setParentGenreId(genre.getParentGenra().getId());
            dto.setParentGenreName(genre.getParentGenra().getName());
        }

        if (genre.getSubGenre() != null && !genre.getSubGenre().isEmpty()) {
            dto.setSubGenre(
                genre.getSubGenre().stream()
                    .filter(Genre::getActive)
                    .map(this::toDTO)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Genre toEntity(GenreDTO dto) {
        if (dto == null) return null;

        Genre genre = Genre.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .displayOrder(dto.getDisplayOrder())
                .active(true)
                .build();

        if (dto.getParentGenreId() != null) {
            genreRepository.findById(dto.getParentGenreId())
                .ifPresent(parent -> {
                    genre.setParentGenra(parent);
                    parent.getSubGenre().add(genre);
                });
        }

        return genre;
    }

    public void updateEntityFromDTO(GenreDTO dto, Genre existing) {
        if (dto == null || existing == null) return;

        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setDisplayOrder(dto.getDisplayOrder());
        if (dto.getActive() != null) {
            existing.setActive(dto.getActive());
        }
    }

    public List<GenreDTO> toDTOList(List<Genre> genres) {
        return genres.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

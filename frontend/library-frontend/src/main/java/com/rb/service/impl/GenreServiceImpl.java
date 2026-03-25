package com.rb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rb.exception.GenreException;
import com.rb.mapper.GenreMapper;
import com.rb.model.Genre;
import com.rb.paylode.dto.GenreDTO;
import com.rb.repository.GenreRepository;
import com.rb.service.GenreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDTO createGenre(GenreDTO dto) {
        Genre genre = genreMapper.toEntity(dto);
        return genreMapper.toDTO(genreRepository.save(genre));
    }

    @Override
    public List<GenreDTO> getAllGenre() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDTO getGenreById(Long genreId) throws GenreException {
        Genre genre = genreRepository.findByIdWithRelations(genreId);
        if (genre == null) throw new GenreException("Genre not found");
        return genreMapper.toDTO(genre);
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO dto) throws GenreException {
        Genre existing = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found"));
        genreMapper.updateEntityFromDTO(dto, existing);
        return genreMapper.toDTO(genreRepository.save(existing));
    }

    @Override
    public void deleteGenre(Long genreId) throws GenreException {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found"));
        genre.setActive(false);
        genreRepository.save(genre);
    }

    @Override
    public void hardDeleteGenre(Long genreId) throws GenreException {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found"));
        genreRepository.delete(genre);
    }

    @Override
    public List<GenreDTO> getTopLevelGeres() {
        return genreMapper.toDTOList(
            genreRepository.findByParentGenraIsNullAndActiveTrueOrderByDisplayOrderAsc()
        );
    }

    @Override
    public long getTotalActivesGenres() {
        return genreRepository.countByActiveTrue();
    }
}

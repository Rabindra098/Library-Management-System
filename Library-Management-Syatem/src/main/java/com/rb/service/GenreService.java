package com.rb.service;

import java.util.List;

import com.rb.exception.GenreException;
import com.rb.paylode.dto.GenreDTO;

public interface GenreService {

    GenreDTO createGenre(GenreDTO genre);

    List<GenreDTO> getAllGenre();

    GenreDTO getGenreById(Long genreId) throws GenreException;

    GenreDTO updateGenre(Long genreId, GenreDTO genre) throws GenreException;

    void deleteGenre(Long genreId) throws GenreException;

    void hardDeleteGenre(Long genreId) throws GenreException;

    List<GenreDTO> getTopLevelGeres();

    long getTotalActivesGenres();
}

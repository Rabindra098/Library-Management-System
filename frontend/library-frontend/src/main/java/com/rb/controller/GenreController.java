package com.rb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rb.exception.GenreException;
import com.rb.paylode.dto.GenreDTO;
import com.rb.paylode.response.APIResponse;
import com.rb.service.GenreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    // 🔹 Create Genre
    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO createdGenre = genreService.createGenre(genreDTO);
        return ResponseEntity.ok(createdGenre);
    }

    // 🔹 Get All Genres
    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenre());
    }

    // 🔹 Get Genre By ID
    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id)
            throws GenreException {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    // 🔹 Update Genre
    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(
            @PathVariable Long id,
            @RequestBody GenreDTO genreDTO) throws GenreException {

        GenreDTO updated = genreService.updateGenre(id, genreDTO);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Soft Delete Genre
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> softDeleteGenre(@PathVariable Long id)
            throws GenreException {

        genreService.deleteGenre(id);
        return ResponseEntity.ok(
                new APIResponse("Genre soft deleted successfully", true)
        );
    }

    // 🔹 Hard Delete Genre (Permanent)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<APIResponse> hardDeleteGenre(@PathVariable Long id)
            throws GenreException {

        genreService.hardDeleteGenre(id);
        return ResponseEntity.ok(
                new APIResponse("Genre permanently deleted", true)
        );
    }

    // 🔹 Get Top-Level Genres
    @GetMapping("/top-level")
    public ResponseEntity<List<GenreDTO>> getTopLevelGenres() {
        return ResponseEntity.ok(genreService.getTopLevelGeres());
    }

    // 🔹 Count Active Genres
    @GetMapping("/count")
    public ResponseEntity<Long> countActiveGenres() {
        return ResponseEntity.ok(genreService.getTotalActivesGenres());
    }
}

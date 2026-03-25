package com.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rb.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenraIsNullAndActiveTrueOrderByDisplayOrderAsc();

    long countByActiveTrue();

    @Query("""
        select g from Genre g
        left join fetch g.parentGenra
        left join fetch g.subGenre
        where g.id = :id
    """)
    Genre findByIdWithRelations(@Param("id") Long id);
}

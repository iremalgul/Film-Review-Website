package com.irem.filmAddicts.repository;

import com.irem.filmAddicts.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {

}

package com.irem.filmAddicts.service;

import com.irem.filmAddicts.entity.Film;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface FilmService {
    Film findById(Long id);
}

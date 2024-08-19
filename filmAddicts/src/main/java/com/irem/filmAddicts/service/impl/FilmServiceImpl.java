package com.irem.filmAddicts.service.impl;

import com.irem.filmAddicts.entity.Film;
import com.irem.filmAddicts.repository.FilmRepository;
import com.irem.filmAddicts.service.FilmService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Film findById(Long id) {
        return filmRepository.getById(id); 
    }
}

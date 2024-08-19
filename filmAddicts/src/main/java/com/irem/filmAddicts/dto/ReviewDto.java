package com.irem.filmAddicts.dto;

import com.irem.filmAddicts.entity.Film;
import com.irem.filmAddicts.entity.User;
import jakarta.validation.constraints.NotEmpty;


public class ReviewDto {
    private Long id;
    private User user;
    private Film film;
    private int rating;
    @NotEmpty(message = "The comment is required")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

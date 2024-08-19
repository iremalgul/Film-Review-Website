package com.irem.filmAddicts.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class FilmDto {
    @NotEmpty(message = "The name is required")
    private String name;

    @NotEmpty(message = "The genre is required")
    private String genre;

    @Size(min =10, message = "The description should be at least 10 characters")
    @Size(max =2000, message = "The description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "The release date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    private MultipartFile imageFile;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}


package com.irem.filmAddicts.controller;

import com.irem.filmAddicts.dto.ReviewDto;
import com.irem.filmAddicts.entity.Film;
import com.irem.filmAddicts.dto.FilmDto;
import com.irem.filmAddicts.entity.Review;
import com.irem.filmAddicts.repository.FilmRepository;
import com.irem.filmAddicts.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
public class FilmController {

    @Autowired
    private FilmRepository repo;
    private UserService userService;

    public FilmController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/film/index")
    public String showFilmList(Model model){
        List<Film> films = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("films", films);
        return "admin/film/index";
    }

    @GetMapping("/user/films")
    public String showFilmListUSER(Model model){
        List<Film> films = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("films", films);
        return "user/films";
    }

    @GetMapping("/admin/film/create")
    public String showCreatePage(Model model){
        FilmDto filmDto = new FilmDto();
        model.addAttribute("filmDto", filmDto);
        return "admin/film/CreateFilm";
    }

    @PostMapping("/admin/film/createFilm")
    public String createFilm(@Valid @ModelAttribute FilmDto filmDto, BindingResult result){
       if(filmDto.getImageFile().isEmpty()){
           result.addError(new FieldError("filmDto","imageFile","The image file is required"));
       }
       if (result.hasErrors()){
           return "admin/film/CreateFilm";
       }

       // save image file
        MultipartFile image = filmDto.getImageFile();
        Date releaseDate = new Date();
        String storageFileName = releaseDate.getTime() +"_"+ image.getOriginalFilename();

        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING) ;
            }

        } catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());

        }

        Film film = new Film();
        film.setName(filmDto.getName());
        film.setGenre(filmDto.getGenre());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(releaseDate);
        film.setImageFileName(storageFileName);

        repo.save(film);

        return "redirect:/admin/film/index";
    }

    @GetMapping("/admin/film/edit")
    public String showEditPage(Model model, @RequestParam Long id){
        try{
            Film film = repo.findById(id).get();
            model.addAttribute("film", film);

            FilmDto filmDto = new FilmDto();
            filmDto.setName(film.getName());
            filmDto.setGenre(film.getGenre());
            filmDto.setDescription(film.getDescription());
            filmDto.setReleaseDate(film.getReleaseDate());


            model.addAttribute("filmDto", filmDto);

        }
        catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());
            return "redirect:/admin/film/index";
        }

        return "admin/film/EditFilm";
    }


    @PostMapping("/admin/film/edit")
    public String updateFilm(Model model, @RequestParam Long id, @Valid @ModelAttribute FilmDto filmDto, BindingResult result){
        try{
            Film film = repo.findById(id).get();
            model.addAttribute("film", film);

            if (result.hasErrors()){
                return "admin/film/EditFilm";
            }

            if(!filmDto.getImageFile().isEmpty()){
                // delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + film.getImageFileName());

                try{
                    Files.delete(oldImagePath);
                }
                catch (Exception ex){
                    System.out.println("Exception: "+ ex.getMessage());
                }

                //save new image file
                MultipartFile image = filmDto.getImageFile();
                Date releaseDate = new Date();
                String storageFileName = releaseDate.getTime() +"_"+ image.getOriginalFilename();

                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING) ;
                }

                film.setImageFileName(storageFileName);
            }

            film.setName(filmDto.getName());
            film.setGenre(filmDto.getGenre());
            film.setDescription(filmDto.getDescription());
            film.setReleaseDate(filmDto.getReleaseDate());


            repo.save(film);
        }
        catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());
        }

        return "redirect:/admin/film/index";

    }

    @GetMapping("admin/film/delete")
    public String deleteFilm(@RequestParam Long id){
        try{
            Film film =repo.findById(id).get();

            //delete product image
            Path ImagePath = Paths.get("public/images/" + film.getImageFileName());
            try{
                Files.delete(ImagePath);
            }
            catch (Exception ex){
                System.out.println("Exception: "+ ex.getMessage());
            }

            //delete the product
            repo.delete(film);

        }
        catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());
        }
        return "redirect:/admin/film/index";
    }

    @GetMapping("user/film/detail")
    public String showFilmDetail(@RequestParam Long id,Model model) {
        Film film =repo.findById(id).get();
        model.addAttribute("film", film);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setFilm(film);
        model.addAttribute("reviewDto", reviewDto);

        List<Review> reviews = film.getReviews();
        model.addAttribute("reviews", reviews);

        // Calculate average rating
        double averageRating = calculateAverageRating(reviews);
        model.addAttribute("averageRating", averageRating);
        return"user/filmDetail";
    }

    @GetMapping("admin/film/detail")
    public String showFilmDetailAdmin(@RequestParam Long id,Model model) {
        Film film =repo.findById(id).get();
        model.addAttribute("film", film);

        List<Review> reviews = film.getReviews();
        model.addAttribute("reviews", reviews);

        // Calculate average rating
        double averageRating = calculateAverageRating(reviews);
        model.addAttribute("averageRating", averageRating);
        return"admin/film/filmDetail";
    }


    private double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0; // Return default if no reviews available
        }

        double totalRating = 0.0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / reviews.size();
    }
}

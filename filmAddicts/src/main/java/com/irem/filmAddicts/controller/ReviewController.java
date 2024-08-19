package com.irem.filmAddicts.controller;


import com.irem.filmAddicts.dto.ReviewDto;
import com.irem.filmAddicts.entity.Review;
import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.repository.ReviewRepository;
import com.irem.filmAddicts.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository repo;
    private UserService userService;

    public ReviewController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/createReview")
    public String createReview(@Valid @ModelAttribute ReviewDto reviewDto, BindingResult result){
        if (result.hasErrors()){
            return "user/films";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User authUser = userService.findUserByEmail(userEmail);

         Review review = new Review();
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setFilm(reviewDto.getFilm());
        review.setUser(authUser);
        repo.save(review);

        return "redirect:/user/film/detail?id=" + reviewDto.getFilm().getId();
    }

    @GetMapping("/admin/review/delete")
    public String deleteReview(@RequestParam Long id){
            Review review =repo.findById(id).get();
            repo.delete(review);
        return "redirect:/admin/film/detail?id=" + review.getFilm().getId();
    }

}

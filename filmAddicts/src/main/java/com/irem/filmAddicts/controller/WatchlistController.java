package com.irem.filmAddicts.controller;


import com.irem.filmAddicts.entity.Film;
import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.entity.Watchlist;
import com.irem.filmAddicts.repository.FilmRepository;
import com.irem.filmAddicts.repository.WatchlistRepository;
import com.irem.filmAddicts.service.FilmService;
import com.irem.filmAddicts.service.UserService;
import com.irem.filmAddicts.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class WatchlistController {

    @Autowired
    private WatchlistRepository repo;
    @Autowired
    private FilmRepository filmrepo;
    private UserService userService;
    private FilmService filmService;
    private WatchlistService watchlistService;


    public WatchlistController(UserService userService, WatchlistService watchlistService, FilmService filmService) {
        this.userService = userService;
        this.watchlistService = watchlistService;
        this.filmService = filmService;
    }

    @GetMapping("/user/watchlist")
    public String showUserWatchlist(Model model) {
        // Get the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the authenticated user's email
        String userEmail = authentication.getName();

        User authUser = userService.findUserByEmail(userEmail);

        //Find the authenticated user's watchlist
        Watchlist watchlist = watchlistService.findWatchlistByUser(authUser);

        // Get all films in the watchlist
        List<Film> watchlistFilms = watchlist.getFilms();

        // Add watchlistFilms to the model
        model.addAttribute("watchlistFilms", watchlistFilms);
        return "user/watchlist";
    }

    @GetMapping("/user/addToWatchlist")
    public String addToWatchlist(@RequestParam Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User authUser = userService.findUserByEmail(userEmail);
        Watchlist watchlist = watchlistService.findWatchlistByUser(authUser);

// Check if the film already exists in the watchlist
        boolean duplicateFound = false;
        for (Film film : watchlist.getFilms()) {
            if (film.getId().equals(id)) {
                duplicateFound = true;
                break;
            }
        }

        if (duplicateFound) {
            // Handle duplicate film case (e.g., show a message to the user)
            return "redirect:/user/films?error";
        }else {
            // Film does not exist in the watchlist, add it

            Film filmToAdd = filmService.findById(id);
            watchlist.getFilms().add(filmToAdd);
            repo.save(watchlist);
            return "redirect:/user/films?success";
        }
    }

    @GetMapping("/user/deleteFromWatchlist")
    public String deleteFromWatchlist(@RequestParam Long id){
        try{
            Film film =filmrepo.findById(id).get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User authUser = userService.findUserByEmail(userEmail);
            Watchlist watchlist = watchlistService.findWatchlistByUser(authUser);

            watchlist.getFilms().remove(film);
            repo.save(watchlist);

        }
        catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());
        }
        return "redirect:/user/watchlist";
    }

}
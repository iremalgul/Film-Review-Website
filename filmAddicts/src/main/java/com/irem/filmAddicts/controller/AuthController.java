package com.irem.filmAddicts.controller;

import com.irem.filmAddicts.dto.UserDto;
import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.entity.Watchlist;
import com.irem.filmAddicts.repository.WatchlistRepository;
import com.irem.filmAddicts.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private WatchlistRepository Watchlistrepo;
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/index"})
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findUserByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        User newUser = userService.saveUser(user); // Save the new user

        // create watchlist for new user
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(newUser);
        Watchlistrepo.save(watchlist);

        return "redirect:/register?success";
    }



}

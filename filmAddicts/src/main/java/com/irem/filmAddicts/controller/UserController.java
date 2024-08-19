package com.irem.filmAddicts.controller;

import com.irem.filmAddicts.dto.UserDto;
import com.irem.filmAddicts.entity.User;
import com.irem.filmAddicts.entity.Watchlist;
import com.irem.filmAddicts.repository.UserRepository;
import com.irem.filmAddicts.repository.WatchlistRepository;
import com.irem.filmAddicts.service.UserService;
import com.irem.filmAddicts.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserRepository repo;
    @Autowired
    private WatchlistRepository watchlistrepo;
    private UserService userService;
    private WatchlistService watchlistService;
    public UserController(UserService userService,WatchlistService watchlistService) {
        this.userService = userService;
        this.watchlistService = watchlistService;
    }

    @GetMapping("admin/user/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }


    @GetMapping("admin/user/delete")
    public String deleteUser(@RequestParam Long id){
        try{
            User user =repo.findById(id).get();
            user.getRoles().clear();
            repo.save(user);
            Watchlist watchlist = watchlistService.findWatchlistByUser(user);
            watchlistrepo.delete(watchlist);
            repo.delete(user);

        }
        catch (Exception ex){
            System.out.println("Exception: "+ ex.getMessage());
        }
        return "redirect:/admin/user/users";
    }

    @GetMapping("admin/user/showUsers")
    public String showUserName(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user= userService.findUserByEmail(userEmail);

        model.addAttribute("user", user);

        return "redirect:/admin/user/users";
    }

    @GetMapping("/admin/createAdmin")
    public String showAddAdmin(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "/admin/addAdmin";
    }

    @PostMapping("/admin/save")
    public String AddAdmin(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findUserByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/admin/addAdmin";
        }
        User newUser = userService.saveAdmin(user); // Save the new user

        // create watchlist for new user
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(newUser);
        watchlistrepo.save(watchlist);

        return "redirect:/admin/createAdmin?success";
    }

}

package com.irem.filmAddicts.service;

import com.irem.filmAddicts.dto.UserDto;
import com.irem.filmAddicts.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(UserDto userDto);
    User saveAdmin(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}

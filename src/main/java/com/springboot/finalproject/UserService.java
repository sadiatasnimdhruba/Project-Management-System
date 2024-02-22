package com.springboot.finalproject;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {

   User save(UserRegistrationDto userRegistrationDto);

    List<User> getAllUsers();

    User getUserById(Long memberId);

    User getUserByEmail(String email);
    long getTotalUserCount();

    User getUserByUsername(String username);
    User getUserByFirstName(String firstname);
    User getUserByLastName(String lastname);
    User getUserByAddress(String address);
    User getUserByBirthDate(LocalDate birthdate);
    User getUserByImage(String image);

    User updateUser(UserRegistrationDto userRegistrationDto);


    boolean findByUsername(String username);
}

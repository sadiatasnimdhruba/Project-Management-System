package com.springboot.finalproject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
    User findByUsername(String username);
    User findByFirstname(String firstname);
    User findByLastname(String lastname);
    User findByAddress(String address);
    User findByBirthdate(LocalDate birthdate);
    User findByImage(String image);

}

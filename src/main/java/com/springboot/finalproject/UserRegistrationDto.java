package com.springboot.finalproject;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Lob;
import java.time.LocalDate;

public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String image;

    public UserRegistrationDto(){

    }

    public UserRegistrationDto(String username , String email, String password, String firstname, String lastname, String address, LocalDate birthdate, String image) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname= firstname;
        this.lastname= lastname;
        this.address=address;
        this.image = image;
        this.birthdate=birthdate;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {

        this.birthdate = birthdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}

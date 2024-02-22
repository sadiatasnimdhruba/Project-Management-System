package com.springboot.finalproject;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "unique_email"),
        @UniqueConstraint(columnNames = "username", name = "unique_username")
})
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name =  "password")
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "address")
    private String address;

    @Column(name = "birthdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;


    @Column(name = "image" , columnDefinition = "LONGTEXT")
    private String image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "project_id", referencedColumnName = "id"))

    private Collection<Project> projects;


    public User() {
    }

    public User(String username, String email, String password, String firstname, String lastname, String address, LocalDate birthdate, String image, Collection<Project> projects) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname= firstname;
        this.lastname= lastname;
        this.address=address;
        this.birthdate=birthdate;
        this.image = image;

    }

    public <T> User(String username, String email, String encode, List<T> asList) {
    }

    public User(String username, String email, String firstname, String lastname, String address, String image, LocalDate birthdate, List<Project> projects) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }
}

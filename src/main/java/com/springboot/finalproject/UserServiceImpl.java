package com.springboot.finalproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getUsername(),
                registrationDto.getEmail(),
               new BCryptPasswordEncoder().encode(registrationDto.getPassword()), Arrays.asList(new Project()));

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long memberId) {
        return userRepository.findById(memberId).get();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean findByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public User getUserByFirstName(String firstname){
        return userRepository.findByFirstname(firstname);
    }

    @Override
    public User getUserByLastName(String lastname){
        return userRepository.findByLastname(lastname);
    }

    @Override
    public User getUserByAddress(String address){
        return userRepository.findByAddress(address);
    }
    @Override
    public User getUserByBirthDate(LocalDate birthdate){
        return userRepository.findByBirthdate(birthdate);
    }

    @Override
    public User getUserByImage(String image){
        return userRepository.findByImage(image);
    }

    @Override
    public User updateUser(UserRegistrationDto registrationDto){

        User user = new User(registrationDto.getUsername(),
                registrationDto.getEmail(),
                registrationDto.getFirstname(),
                registrationDto.getLastname(),
                registrationDto.getAddress(),
                registrationDto.getImage(),
                registrationDto.getBirthdate(),
                Arrays.asList(new Project()));

        return userRepository.save(user);
    }

    @Override
    public long getTotalUserCount() {
        return userRepository.count();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getProjects()));
    }

    private  Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Project> projects){
        return projects.stream().map(project -> new SimpleGrantedAuthority(project.getName())).collect(Collectors.toList());
    }


}

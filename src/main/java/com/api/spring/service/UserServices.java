package com.api.spring.service;

import com.api.spring.controller.PersonController;
import com.api.spring.exceptions.RequiredObjectIsNullException;
import com.api.spring.exceptions.ResourceNotFoundException;
import com.api.spring.mapper.ModelMapperConverter;
import com.api.spring.model.Person;
import com.api.spring.repository.PersonRepository;
import com.api.spring.repository.UserRepository;
import com.api.spring.vo.PersonVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {
    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if(user != null) {
            return user;
        }
        else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
    }
}

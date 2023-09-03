package com.example.CloudStorage.service;

import com.example.CloudStorage.entity.Role;
import com.example.CloudStorage.entity.User;
import com.example.CloudStorage.model.EnumRoles;
import com.example.CloudStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    //todo:оставить или убрать
    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByLogin(user.getLogin()).get();

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(EnumRoles.ROLE_USER)));
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
}

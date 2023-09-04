package com.example.CloudStorage.service;

import com.example.CloudStorage.entity.User;
import com.example.CloudStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        User userDetails = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
        userDetails.setPassword(new BCryptPasswordEncoder().encode(userDetails.getPassword()));
        return userDetails;
    }


//    public boolean saveUser(User user) {
//        User userFromDB = userRepository.findByLogin(user.getLogin()).get();
//
//        if (userFromDB != null) {
//            return false;
//        }
//
//        user.setRoles(Collections.singleton(new Role(EnumRoles.ROLE_USER)));
//        user.setPassword(encoder.encode(user.getPassword()));
//        userRepository.save(user);
//        return true;
//    }
}

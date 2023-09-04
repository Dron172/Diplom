package com.example.CloudStorage.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Repository
public class CloudRepository {

    public Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    public Optional<UserDetails> login(String authToken, UserDetails userPrincipal){
        return ofNullable(tokenStorage.put(authToken, userPrincipal));
    }

    public Optional<UserDetails> logout(String authToken) {
         return ofNullable(tokenStorage.remove(authToken));
    }
}

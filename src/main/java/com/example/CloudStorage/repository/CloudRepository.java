package com.example.CloudStorage.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CloudRepository {
    public Optional<String> login(String login, String password){
        //TODO: authorization manipulations
        return Optional.of(null);
    }

    public void logout(String authToken) {
        //TODO: reset or delete token
    }
}

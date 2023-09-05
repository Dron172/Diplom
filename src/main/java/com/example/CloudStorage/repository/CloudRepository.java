package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Repository
public class CloudRepository {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    public Optional<UserDetails> login(String authToken, UserDetails userPrincipal){
        return ofNullable(tokenStorage.put(authToken, userPrincipal));
    }

    public Optional<UserDetails> logout(String authToken) {
         return ofNullable(tokenStorage.remove(authToken));
    }

    public Optional<File> uploadFile(File file, String authToken) {
        Optional<Long> userId = getUserId(authToken);
        if (!userId.isEmpty()) {
            file.setUserId(userId.get());
            return ofNullable(fileRepository.save(file));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<List<File>> getFiles(String authToken, int limit) {
        Optional<Long> userId = getUserId(authToken);
        if (!userId.isEmpty()) {
            return ofNullable(fileRepository.findAllByUserId(userId.get()));
        }
        return Optional.empty();
    }

    private Optional<Long> getUserId(String authToken) {
        UserDetails user = tokenStorage.get(authToken.substring(7));
        return user != null ? ofNullable(userRepository.findByLogin(user.getUsername()).get().getId()) : Optional.empty();
    }

}
